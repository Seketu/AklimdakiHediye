package com.reylortechnology.aklimdakihediye.LocalDatabase.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Notifications
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationsDao {

    @Query("SELECT * FROM notifications")
    fun getAllNotifications(): Flow<List<Notifications>>

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotificationsOnce(): List<Notifications>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notifications): Long

    @Update
    suspend fun updateNotification(notification: Notifications)

    @Delete
    suspend fun deleteNotification(notification: List<Notifications>)
}
