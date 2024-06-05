package com.duyle.roomdbex2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.duyle.roomdbex2.data.StudentDB
import com.duyle.roomdbex2.ui.component.DialogDelete
import com.duyle.roomdbex2.ui.component.DialogEdit
import com.duyle.roomdbex2.ui.theme.RoomDBEx2Theme

class MainActivity : ComponentActivity() {
    private val database by lazy {
       Room.databaseBuilder(
           applicationContext,
           StudentDB::class.java,
           "student.db"
       ).build()
    }

    private val viewModel by viewModels<StudentViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun<T: ViewModel> create(modelClass: Class<T>): T {
                    return StudentViewModel(database.dao) as T
                }
            }
        }
    )



    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomDBEx2Theme {
                val state by viewModel.state.collectAsState()

                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(16.dp)) { innerPadding ->
                    HomeScreen(
                        state = state,
                        onEven = viewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen (
    state: StudentState,
    onEven: (StudentEvent)-> Unit
) {
    var showDialogEdit by remember { mutableStateOf(false) }
    var showDialogDelete by remember { mutableStateOf(false) }

    if (showDialogDelete){
        DialogDelete(
            state = state,
            onDismiss = { showDialogDelete = false },
            onEven = onEven
        )
    }


    if (showDialogEdit){
        DialogEdit(
            state = state,
            onDismiss = { showDialogEdit = false },
            onEvent = onEven)
    }

    Column (Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Quan ly Sinh vien",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(value = state.hoten.value,
            onValueChange = { state.hoten.value = it },
            label = { Text("Họ tên") }
        )

        OutlinedTextField(value = state.diemTB.value,
            onValueChange = { state.diemTB.value = it },
            label = { Text("Điểm") },
            keyboardOptions =
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

        )

        OutlinedTextField(value = state.mssv.value,
            onValueChange = { state.mssv.value = it },
            label = { Text("Mã sinh viên") }
        )

        val context = LocalContext.current
        Button(modifier = Modifier.padding(top = 10.dp),onClick = {
            val name = state.hoten.value
            val mssv = state.mssv.value
            val diem = state.diemTB.value
            if (name.isBlank() || mssv.isBlank() || diem.isBlank()) {
                Toast.makeText(context, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show()

            }else if ((diem.toFloatOrNull() ?: -1f) < 0 || (diem.toFloatOrNull() ?: -1f) > 10) {
                Toast.makeText(context, "Điểm phải là số từ 0 >10", Toast.LENGTH_SHORT).show()
            }
            else
            onEven(StudentEvent.SaveStudent)
        }) {
            Text(text = "Thêm SV")
        }

        LazyColumn {

            items(state.students) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(modifier = Modifier, text = it.uid.toString())

                    Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                        Text(modifier = Modifier, text ="Họ tên:  "+ it.hoten.toString())
                        Text(modifier = Modifier.padding(vertical = 3.dp), text ="MSSV:  "+ it.mssv.toString())
                        Text(modifier = Modifier, text ="Điểm:  "+ it.diemTB.toString())
                    }


                    IconButton(onClick = {
                        state.selectedStudent.value = it
                        showDialogEdit=true
                    }) {
                        Icon(Icons.Default.Edit, contentDescription = "")
                    }
                    IconButton(onClick = {
                        state.selectedStudent.value = it

                        showDialogDelete  = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "")
                    }
                }
                Divider()
            }
        }
    }
}

