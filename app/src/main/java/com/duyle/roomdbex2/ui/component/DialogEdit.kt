package com.duyle.roomdbex2.ui.component

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.duyle.roomdbex2.StudentEvent
import com.duyle.roomdbex2.StudentState
import com.duyle.roomdbex2.data.StudentModel

@Composable
fun DialogEdit(
    state: StudentState,
    onDismiss: () -> Unit,
    onEvent: (StudentEvent) -> Unit
) {


    LaunchedEffect(key1 = true) {
        state.hotenUpdate.value = state.selectedStudent.value.hoten.toString()
        state.mssvUpdate.value = state.selectedStudent.value.mssv.toString()
        state.diemTBUpdate.value = state.selectedStudent.value.diemTB.toString()
    }


    Dialog(onDismissRequest = onDismiss) {

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = "Sửa sinh vien",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },

            text = {
                Column {
                    OutlinedTextField(value = state.hotenUpdate.value,
                        onValueChange = { state.hotenUpdate.value = it },
                        label = { Text("Họ tên") }
                    )

                    OutlinedTextField(value = state.diemTBUpdate.value,
                        onValueChange = { state.diemTBUpdate.value = it },
                        label = { Text("Điểm") },
                        keyboardOptions =
                        KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),

                        )

                    OutlinedTextField(value = state.mssvUpdate.value,
                        onValueChange = { state.mssvUpdate.value = it },
                        label = { Text("Mã sinh viên") }
                    )

                }

            },
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            val name = state.hotenUpdate.value
                            val mssv = state.mssvUpdate.value
                            val diem = state.diemTBUpdate.value
                            if (name.isBlank() || mssv.isBlank() || diem.isBlank()) {
                                Toast.makeText(context, "Nhập thiếu thông tin", Toast.LENGTH_SHORT).show()

                            }else if ((diem.toFloatOrNull() ?: -1f) < 0 || (diem.toFloatOrNull() ?: -1f) > 10) {
                                Toast.makeText(context, "Điểm phải là số từ 0 >10", Toast.LENGTH_SHORT).show()
                            }
                            else{
                                onEvent(StudentEvent.EditStudent)
                                onDismiss()
                            }



                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB703),
                            contentColor = Color.White
                        ),
                    ) {
                        Text("Lưu")
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFFB703),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.weight(1f),

                        ) {
                        Text("Hủy")
                    }
                }

            }
        )
    }
}