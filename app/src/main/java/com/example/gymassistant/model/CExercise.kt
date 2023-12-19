package com.example.gymassistant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise")
data class CExercise(
    @PrimaryKey
    var id: String? = "",
    @ColumnInfo
    var workout_id: String? = "",
    @ColumnInfo
    var name: String? = "",
    @ColumnInfo
    var description: String? = "",
    @ColumnInfo
    var duration: Int? = -1,
    @ColumnInfo
    var num_sets: Int? = -1,
    @ColumnInfo
    var weight: Double? = -1.0,
    @ColumnInfo
    var times_per_set: Int? = -1
){
}