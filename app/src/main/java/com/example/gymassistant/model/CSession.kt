package com.example.gymassistant.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session")
class CSession (
    @PrimaryKey
    val id: String,

    @ColumnInfo
    var workout_id: String?

)

