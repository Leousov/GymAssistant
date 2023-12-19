package com.example.gymassistant.model

data class CExercise(
    var id: String? = "",
    var workout_id: String? = "",
    var name: String? = "",
    var description: String? = "",
    var duration: Int? = -1,
    var num_sets: Int? = -1,
    var weight: Double? = -1.0,
    var times_per_set: Int? = -1
){
}