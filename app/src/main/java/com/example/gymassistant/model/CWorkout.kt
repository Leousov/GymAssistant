package com.example.gymassistant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Определение сущности тренировки для базы данных
@Entity(tableName = "workout")
data class CWorkout(
    @PrimaryKey
    val id: String, // Идентификатор тренировки (первичный ключ)

    @ColumnInfo
    var title: String?, // Название тренировки

    @ColumnInfo
    var text: String?, // Текстовое описание тренировки

    @ColumnInfo
    val user_id: String? // Идентификатор пользователя, создавшего тренировку
)
