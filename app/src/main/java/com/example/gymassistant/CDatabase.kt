package com.example.gymassistant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymassistant.model.CExercise
import com.example.gymassistant.model.CWorkout

@Database(
    entities = [CWorkout::class, CExercise::class],
    version = 1
)
abstract class CDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: CDatabase? = null
        fun getDatabase(context: Context): CDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room
                            .databaseBuilder(
                                context,
                                CDatabase::class.java,
                                "database.db"
                            )
                            .build()
                }
            }
            return INSTANCE!!
        }
    }
}