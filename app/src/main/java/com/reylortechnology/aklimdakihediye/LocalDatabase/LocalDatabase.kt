package com.reylortechnology.aklimdakihediye.LocalDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts


@Database(entities = arrayOf(LocalUserInformation::class,SavedGifts::class,Notifications::class), version = 6)
abstract class LocalDatabase(): RoomDatabase() {
    abstract fun userInformationDao() : UserInformationDao
    abstract fun savedGiftsDao() : SavedGiftDao
    abstract fun notificationsDao() : NotificationsDao

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