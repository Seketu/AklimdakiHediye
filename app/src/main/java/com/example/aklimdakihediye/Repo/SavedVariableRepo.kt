package com.example.aklimdakihediye.Repo

import com.example.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.example.aklimdakihediye.LocalDatabase.Models.SavedGifts
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