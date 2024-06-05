package com.duyle.roomdbex2.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [StudentModel::class]
    , version = 1
)
abstract class StudentDB : RoomDatabase() {
    abstract val dao: StudentDAO
}

