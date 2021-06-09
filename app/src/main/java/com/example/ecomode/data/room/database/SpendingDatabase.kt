package com.example.ecomode.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecomode.data.room.Converters
import com.example.ecomode.data.room.dao.SpendingDao
import com.example.ecomode.data.room.dao.UserDao
import com.example.ecomode.data.room.entity.SpendingEntity
import com.example.ecomode.data.room.entity.UserEntity

@Database(
    entities = [
        UserEntity:: class,
        SpendingEntity:: class
    ],
    version = 1,
    exportSchema = false

)
@TypeConverters(Converters::class)
abstract class SpendingDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun spendingDao(): SpendingDao

    companion object {
        private const val DBNAME = "spending_database"

        @Volatile
        private var INSTANCE: SpendingDatabase? = null

        fun getDatabase(context: Context): SpendingDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpendingDatabase::class.java,
                    DBNAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}