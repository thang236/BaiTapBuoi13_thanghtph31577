package com.duyle.roomdbex2

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.duyle.roomdbex2.data.StudentModel

data class StudentState (
    val students: List<StudentModel> = emptyList(),
    val selectedStudent: MutableState<StudentModel> = mutableStateOf(StudentModel(-1, "thang", "ph31577", 10f)),
    var hoten: MutableState<String> = mutableStateOf(""),
    val mssv: MutableState<String> = mutableStateOf(""),
    val diemTB: MutableState<String> = mutableStateOf(""),

    var hotenUpdate: MutableState<String> = mutableStateOf(""),
    val mssvUpdate: MutableState<String> = mutableStateOf(""),
    val diemTBUpdate: MutableState<String> = mutableStateOf("")

)