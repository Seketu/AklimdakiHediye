package com.reylortechnology.aklimdakihediye.LocalDatabase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedGiftDao {
    @Query("SELECT * FROM saved_gifts")
    fun getAllSavedGifts(): Flow<List<SavedGifts>>

    @Insert
    suspend fun addSavedGift(savedGift: SavedGifts)

    @Query("DELETE FROM saved_gifts where giftId in (:giftIdList)")
    suspend fun deleteAllSavedGifts(giftIdList: List<Int>)

}