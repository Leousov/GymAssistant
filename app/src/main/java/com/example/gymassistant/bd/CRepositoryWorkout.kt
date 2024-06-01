package com.example.gymassistant.bd

import android.content.Context
import com.example.gymassistant.CDatabase
import com.example.gymassistant.model.CWorkout
import kotlinx.coroutines.flow.Flow

// Репозиторий для работы с таблицей тренировок в базе данных
class CRepositoryWorkout (
    context : Context
) {
    // Получаем экземпляр базы данных
    private val db = CDatabase.getDatabase(context)
    // Получаем DAO для работы с таблицей тренировок
    private val daoWorkout = db.daoWorkout()

    // Функция для получения всех тренировок из базы данных в виде потока
    fun getAll(): Flow<List<CWorkout>> {
        return daoWorkout.getAll()
    }

    // Функция для получения тренировки по её ID в виде потока
    fun getById(
        id: String
    ): Flow<CWorkout?> {
        return daoWorkout.getById(id)
    }

    // Функция для вставки новой тренировки в базу данных
    // Вызов этой функции должен быть выполнен в корутине
    suspend fun insert(
        workout: CWorkout
    ) {
        daoWorkout.insert(workout)
    }

    // Функция для удаления тренировки из базы данных
    // Вызов этой функции должен быть выполнен в корутине
    suspend fun delete(
        workout: CWorkout
    ) {
        daoWorkout.delete(workout)
    }
}
