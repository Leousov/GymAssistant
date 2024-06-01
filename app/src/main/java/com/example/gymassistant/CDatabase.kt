package com.example.gymassistant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymassistant.dao.IDAOExercise
import com.example.gymassistant.dao.IDAOWorkout
import com.example.gymassistant.model.CExercise
import com.example.gymassistant.model.CWorkout

// Аннотация @Database указывает, что это класс базы данных Room
@Database(
    entities = [CWorkout::class, CExercise::class], // Сущности, включенные в базу данных
    version = 1 // Версия базы данных
)
abstract class CDatabase : RoomDatabase() {

    // Абстрактные методы для получения DAO объектов
    abstract fun daoExercise(): IDAOExercise
    abstract fun daoWorkout(): IDAOWorkout

    // Компаньон-объект для реализации Singleton паттерна
    companion object {
        @Volatile
        private var INSTANCE: CDatabase? = null

        // Метод для получения экземпляра базы данных
        fun getDatabase(context: Context): CDatabase {
            // Если экземпляр базы данных еще не создан, синхронизируем создание
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE =
                        Room
                            .databaseBuilder(
                                context,
                                CDatabase::class.java,
                                "database.db" // Имя файла базы данных
                            )
                            .build() // Создаем базу данных
                }
            }
            return INSTANCE!! // Возвращаем экземпляр базы данных
        }
    }
}
