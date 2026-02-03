package com.reylortechnology.aklimdakihediye.Repo

import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import javax.inject.Inject


class PeopleRepo
    @Inject constructor(
    val peoplesDao: PeoplesDao
) {
    suspend fun addPeople(
        peoples: Peoples
    ){
        try {
            peoplesDao.insertNewPeople(peoples = peoples)
        }catch (
            e : Exception
        ){

        }
    }
}