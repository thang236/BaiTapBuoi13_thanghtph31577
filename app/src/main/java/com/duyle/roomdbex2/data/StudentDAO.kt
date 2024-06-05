package com.duyle.roomdbex2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.duyle.roomdbex2.data.StudentModel
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDAO {

    @Query("SELECT * FROM StudentModel")
    fun getAll(): Flow<List<StudentModel>>

    @Query("SELECT * FROM StudentModel WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<StudentModel>

    @Insert
    suspend fun insert( users: StudentModel)

    @Delete
    suspend fun delete(user: StudentModel)
    @Update
    suspend fun edit(user: StudentModel)
}