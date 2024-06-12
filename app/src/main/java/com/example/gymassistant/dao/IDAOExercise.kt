package com.example.gymassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.gymassistant.model.CExercise
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) для доступа к таблице упражнений
@Dao
interface IDAOExercise {
    // Функция для получения всех упражнений из базы данных
    @Query("SELECT * FROM exercise")
    fun getAll(): Flow<List<CExercise>>

    // Функция для получения упражнения по его ID
    @Query("SELECT * FROM exercise WHERE id = :id")
    fun getById(
        id: String
    ): Flow<CExercise>
    @Query("SELECT * FROM exercise WHERE workout_id = :workout_id")
    fun getExercisesByWorkoutId(workout_id: String): Flow<List<CExercise>>


    // Функция для вставки нового упражнения в базу данных
    // Если упражнение с таким ID уже существует, оно будет заменено (REPLACE)
    @Insert(onConflict = REPLACE)
    suspend fun insert(exercise: CExercise)

    // Функция для вставки списка упражнений в базу данных
    // Если упражнения с такими ID уже существуют, они будут заменены (REPLACE)
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(exercises: List<CExercise>)

    // Функция для обновления существующего упражнения в базе данных
    @Update
    suspend fun update(exercise: CExercise)

    // Функция для удаления упражнения из базы данных
    @Delete
    suspend fun delete(exercise: CExercise)
}
