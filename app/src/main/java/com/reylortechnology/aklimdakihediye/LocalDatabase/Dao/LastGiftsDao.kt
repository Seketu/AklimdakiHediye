package com.reylortechnology.aklimdakihediye.LocalDatabase.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LastGifts
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import kotlinx.coroutines.flow.Flow

@Dao
interface LastGiftsDao {

    @Query("select * from LAST_GIFTS where people_id = :peopleId")
    fun getUserLastGifts(peopleId : Int) : Flow<List<LastGiftsDao>>

    @Insert
    suspend fun addGift(lastGifts: LastGifts)

    @Delete
    suspend fun deleteGift(gifts: LastGifts)
}