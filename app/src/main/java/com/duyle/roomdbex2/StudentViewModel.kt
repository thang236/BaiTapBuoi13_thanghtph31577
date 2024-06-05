package com.duyle.roomdbex2

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyle.roomdbex2.data.StudentDAO
import com.duyle.roomdbex2.data.StudentModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentViewModel (
    private val dao : StudentDAO
) : ViewModel() {

    private var studens = dao.getAll().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(), emptyList()
    )
    val _state = MutableStateFlow(StudentState())

    val state = combine(_state, studens) { state, students ->
        state.copy(
            students = students
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StudentState()
    )


    fun onEvent(event: StudentEvent) {
        when (event) {
            is StudentEvent.SaveStudent -> {



                val student  = StudentModel(
                    hoten = state.value.hoten.value,
                    diemTB = state.value.diemTB.value.toFloatOrNull()?:-1f,
                    mssv = state.value.mssv.value
                )


                viewModelScope.launch {
                    dao.insert(student)
                }
                _state.update {
                    it.copy(
                        hoten = mutableStateOf("") ,
                        diemTB =  mutableStateOf(""),
                        mssv = mutableStateOf("")
                    )
                }

            }
            is StudentEvent.DeleteStudent -> {
                viewModelScope.launch {
                    dao.delete(event.student)
                }
            }

            is StudentEvent.EditStudent -> {


                viewModelScope.launch {
                    state.value.selectedStudent.value.hoten = state.value.hotenUpdate.value
                    state.value.selectedStudent.value.diemTB = state.value.diemTBUpdate.value.toFloatOrNull()?:-1f
                    state.value.selectedStudent.value.mssv = state.value.mssvUpdate.value
                    dao.edit( state.value.selectedStudent.value)
                }

                _state.update {
                    it.copy(
                        hoten = mutableStateOf("") ,
                        diemTB = mutableStateOf(""),
                        mssv = mutableStateOf("")
                    )
                }
            }
        }


    }
}