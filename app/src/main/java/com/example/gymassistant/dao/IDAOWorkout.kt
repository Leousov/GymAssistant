package com.example.gymassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gymassistant.model.CWorkout
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) для доступа к таблице тренировок
@Dao
interface IDAOWorkout {
    // Функция для получения всех тренировок из базы данных
    @Query("SELECT * FROM workout")
    fun getAll(): Flow<List<CWorkout>>

    // Функция для получения тренировки по её ID
    @Query("SELECT * FROM workout WHERE id = :id")
    fun getById(
        id: String
    ): Flow<CWorkout>

    // Функция для вставки новой тренировки в базу данных
    // Если тренировка с таким ID уже существует, она будет заменена (REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: CWorkout)

    // Функция для вставки списка тренировок в базу данных
    // Если тренировки с такими ID уже существуют, они будут заменены (REPLACE)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workouts: List<CWorkout>)

    // Функция для обновления существующей тренировки в базе данных
    @Update
    suspend fun update(workout: CWorkout)

    // Функция для удаления тренировки из базы данных
    @Delete
    suspend fun delete(workout: CWorkout)
}
