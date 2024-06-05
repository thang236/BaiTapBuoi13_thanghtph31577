package com.duyle.roomdbex2

import com.duyle.roomdbex2.data.StudentModel

sealed interface StudentEvent {
    data object SaveStudent: StudentEvent

    data class DeleteStudent(val student: StudentModel): StudentEvent
    data object EditStudent :StudentEvent


}