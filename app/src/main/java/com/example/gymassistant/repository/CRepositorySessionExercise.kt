package com.example.gymassistant.repository

import android.content.Context
import com.example.gymassistant.CDatabase
import com.example.gymassistant.model.CSessionExercise
import kotlinx.coroutines.flow.Flow

// Репозиторий для работы с таблицей упражнений в базе данных
class CRepositorySessionExercise (
    context : Context
) {
    // Получаем экземпляр базы данных
    private val db = CDatabase.getDatabase(context)
    // Получаем DAO для работы с таблицей упражнений
    private val daoSessionExercise = db.daoSessionExercise()

    // Функция для получения всех упражнений из базы данных в виде потока
    fun getAll(): Flow<List<CSessionExercise>> {
        return daoSessionExercise.getAll()
    }
    fun getExercisesByWorkoutId(workout_id: String): Flow<List<CSessionExercise>> {
        return daoSessionExercise.getExercisesByWorkoutId(workout_id)
    }
    fun getByExerciseID(exercise_id: String): Flow<List<CSessionExercise>> {
        return daoSessionExercise.getByExerciseID(exercise_id)
    }
    fun generateExercisesByWorkoutId(workout_id: String, session_id: String): Flow<List<CSessionExercise>> {
        return daoSessionExercise.generateExercisesByWorkoutId(workout_id, session_id)
    }

    // Функция для получения упражнения по его ID в виде потока
    fun getById(
        id : String
    ) : Flow<CSessionExercise?> {
        return daoSessionExercise.getById(id)
    }

    // Функция для вставки нового упражнения в базу данных
    // Вызов этой функции должен быть выполнен в корутине
    suspend fun insert(
        session_exercise: CSessionExercise
    ) {
        daoSessionExercise.insert(session_exercise)
    }
    suspend fun update(
        session_exercise: CSessionExercise
    ) {
        daoSessionExercise.update(
            session_exercise
            )
    }
    // Функция для удаления упражнения из базы данных
    // Вызов этой функции должен быть выполнен в корутине
    suspend fun delete(
        session_exercise: CSessionExercise
    ) {
        daoSessionExercise.delete(session_exercise)
    }
    suspend fun insertAll(sessionExercises: List<CSessionExercise>) {
        daoSessionExercise.insertAll(sessionExercises)
    }
}
