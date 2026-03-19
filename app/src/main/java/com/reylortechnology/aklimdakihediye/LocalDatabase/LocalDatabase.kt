package com.reylortechnology.aklimdakihediye.LocalDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.reylortechnology.aklimdakihediye.LocalDatabase.Converters.Converters
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LastGifts
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts


@Database(
    entities = [
        LocalUserInformation::class,
        SavedGifts::class,
        Notifications::class,
        Peoples::class,
        LastGifts::class
    ],
    version = 10
)
@TypeConverters(Converters::class)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userInformationDao(): UserInformationDao
    abstract fun savedGiftsDao(): SavedGiftDao
    abstract fun notificationsDao(): NotificationsDao

    abstract fun peoplesDao(): PeoplesDao
}
