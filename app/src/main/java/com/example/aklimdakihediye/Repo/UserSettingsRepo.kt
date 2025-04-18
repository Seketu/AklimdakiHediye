package com.example.aklimdakihediye.Repo

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

    suspend fun addUserInformation(localUserInformation: LocalUserInformation){
        userInformationDao.addLocalInformation(localUserInformation)
    }

}