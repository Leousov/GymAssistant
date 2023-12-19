package com.example.gymassistant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout")
data class CWorkout(
    @PrimaryKey
    val id: String?,
    @ColumnInfo
    var title: String?,
    @ColumnInfo
    var text: String?,
    @ColumnInfo
    val user_id: String?
){
}