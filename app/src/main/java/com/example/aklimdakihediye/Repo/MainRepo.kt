package com.example.aklimdakihediye.Repo

import android.content.Context
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.LocalDatabase
import com.example.aklimdakihediye.LocalDatabase.LocalDatabaseEnit
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MainRepo
    @Inject constructor(private val userInformationDao: UserInformationDao) {

        val getUserInformation = userInformationDao.getLocalInformation()

        suspend fun addInformation (userInformation: LocalUserInformation) {
            userInformationDao.addLocalInformation(userInformation)
        }
    }