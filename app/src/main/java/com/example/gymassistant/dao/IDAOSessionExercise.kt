package com.example.gymassistant.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.example.gymassistant.model.CSessionExercise
import kotlinx.coroutines.flow.Flow

// DAO (Data Access Object) для доступа к таблице упражнений
@Dao
interface IDAOSessionExercise {
    // Функция для получения всех упражнений из базы данных
    @Query("SELECT * FROM session_exercise")
    fun getAll(): Flow<List<CSessionExercise>>

    // Функция для получения упражнения по его ID
    @Query("SELECT * FROM session_exercise WHERE id = :id")
    fun getById(
        id: String
    ): Flow<CSessionExercise>
    @Query("SELECT * FROM session_exercise WHERE workout_id = :workout_id")
    fun getExercisesByWorkoutId(workout_id: String): Flow<List<CSessionExercise>>
    @Query("SELECT * FROM session_exercise WHERE id = :exercise_id")
    fun getByExerciseID(exercise_id: String): Flow<List<CSessionExercise>>
    @Query("""select 
p.id, 
:session_id as session_id, 
:workout_id as workout_id,
p.name,
p.description,
p.duration,
p.num_sets,
p.weight,
p.times_per_set,
0 as sets_done
from (SELECT * FROM exercise WHERE workout_id = :workout_id) p""")
    fun generateExercisesByWorkoutId(workout_id: String, session_id: String): Flow<List<CSessionExercise>>

    // Функция для вставки нового упражнения в базу данных
    // Если упражнение с таким ID уже существует, оно будет заменено (REPLACE)
    @Insert(onConflict = REPLACE)
    suspend fun insert(session_exercise: CSessionExercise)

    // Функция для вставки списка упражнений в базу данных
    // Если упражнения с такими ID уже существуют, они будут заменены (REPLACE)
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(session_exercises: List<CSessionExercise>)

    // Функция для обновления существующего упражнения в базе данных
    @Update
    suspend fun update(session_exercise: CSessionExercise)

    // Функция для удаления упражнения из базы данных
    @Delete
    suspend fun delete(session_exercise: CSessionExercise)
}
