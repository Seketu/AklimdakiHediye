package com.reylortechnology.aklimdakihediye.Repo

import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.SavedGifts
import javax.inject.Inject

class SavedVariableRepo
@Inject constructor(
    private val savedGiftDao: SavedGiftDao
){
    val savedGifts = savedGiftDao.getAllSavedGifts()

    suspend fun saveGift(
        giftForSave : SavedGifts
    ){
        savedGiftDao.addSavedGift(giftForSave)
    }

    suspend fun deleteGift(
        giftId : List<Int>
    ){
        savedGiftDao.deleteAllSavedGifts(giftId)
    }
}