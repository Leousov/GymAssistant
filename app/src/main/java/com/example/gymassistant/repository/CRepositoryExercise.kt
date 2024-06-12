package com.example.gymassistant.repository

import android.content.Context
import com.example.gymassistant.CDatabase
import com.example.gymassistant.model.CExercise
import kotlinx.coroutines.flow.Flow

// Репозиторий для работы с таблицей упражнений в базе данных
class CRepositoryExercise (
    context : Context
) {
    // Получаем экземпляр базы данных
    private val db = CDatabase.getDatabase(context)
    // Получаем DAO для работы с таблицей упражнений
    private val daoExercise = db.daoExercise()

    // Функция для получения всех упражнений из базы данных в виде потока
    fun getAll(): Flow<List<CExercise>> {
        return daoExercise.getAll()
    }
    fun getExercisesByWorkoutId(workout_id: String): Flow<List<CExercise>> {
        return daoExercise.getExercisesByWorkoutId(workout_id)
    }

    // Функция для получения упражнения по его ID в виде потока
    fun getById(
        id : String
    ) : Flow<CExercise?> {
        return daoExercise.getById(id)
    }

    // Функция для вставки нового упражнения в базу данных
    // Вызов этой функции должен быть выполнен в корутине
    suspend fun insert(
        exercise: CExercise
    ) {
        daoExercise.insert(exercise)
    }

    // Функция для удаления упражнения из базы данных
    // Вызов этой функции должен быть выполнен в корутине
    suspend fun delete(
        exercise: CExercise
    ) {
        daoExercise.delete(exercise)
    }
}
