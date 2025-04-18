package com.example.aklimdakihediye.LocalDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.example.aklimdakihediye.LocalDatabase.Models.SavedGifts


@Database(entities = arrayOf(LocalUserInformation::class,SavedGifts::class), version = 2)
abstract class LocalDatabase(): RoomDatabase() {
    abstract fun userInformationDao() : UserInformationDao
    abstract fun savedGiftsDao() : SavedGiftDao

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