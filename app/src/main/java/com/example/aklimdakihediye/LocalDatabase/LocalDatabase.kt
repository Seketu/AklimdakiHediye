package com.example.aklimdakihediye.LocalDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation


@Database(entities = arrayOf(LocalUserInformation::class), version = 1)
abstract class LocalDatabase(): RoomDatabase() {
    abstract fun userInformationDao() : UserInformationDao

    companion object {
        @Volatile
        private var INSTANCE : LocalDatabase? = null

        fun getDatabase(context: Context) : LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "app_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}