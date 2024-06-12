package com.example.gymassistant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Определение сущности упражнения для базы данных
@Entity(tableName = "session_exercise")
data class CSessionExercise(
    @PrimaryKey
    var id: String = "", // Идентификатор упражнения (первичный ключ)

    @ColumnInfo
    var session_id: String? = "",

    @ColumnInfo
    var workout_id: String? = "", // Идентификатор тренировки, к которой относится упражнение

    @ColumnInfo
    var name: String? = "", // Название упражнения

    @ColumnInfo
    var description: String? = "", // Описание упражнения

    @ColumnInfo
    var duration: Int? = -1, // Продолжительность упражнения

    @ColumnInfo
    var num_sets: Int? = -1, // Количество подходов

    @ColumnInfo
    var weight: Int? = -1, // Вес

    @ColumnInfo
    var times_per_set: Int? = -1, // Количество повторений в подходе

    @ColumnInfo
    var sets_done: Int? = -1 // Количество подходов
)
