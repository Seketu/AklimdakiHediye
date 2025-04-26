package com.example.aklimdakihediye.Repo

import android.util.Log
import com.example.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import javax.inject.Inject

class UserSettingsRepo
@Inject constructor(
    private val userInformationDao: UserInformationDao,
    private val savedGiftDao: SavedGiftDao
){
    val getUserInformation = userInformationDao.getLocalInformation()

    suspend fun updateUserInformation(localUserInformation: LocalUserInformation){
        try {
            userInformationDao.updateLocalInformation(
                name = localUserInformation.name,
                bestSide = localUserInformation.bestSide,
                guid = localUserInformation.uid,
                zodiac = localUserInformation.zodiac,
                old = localUserInformation.old,
                caracter = localUserInformation.caracter,
                hobbies = localUserInformation.hobbies
            )
        }catch (e: Exception){
            Log.e("UserSettingsRepo", "updateUserInformation: ${e.message}")
        }

    }

    suspend fun addUserInformation(localUserInformation: LocalUserInformation){
        userInformationDao.addLocalInformation(localUserInformation)
    }

}