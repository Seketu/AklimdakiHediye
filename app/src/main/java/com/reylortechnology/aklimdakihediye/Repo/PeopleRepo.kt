package com.reylortechnology.aklimdakihediye.Repo

import android.util.Log
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import javax.inject.Inject


class PeopleRepo
    @Inject constructor(
    val peoplesDao: PeoplesDao
) {


        val peoples = peoplesDao.getAllPeoples()

    suspend fun addPeople(
        peoples: Peoples
    ) : Result<Unit>{
        try {
            peoplesDao.insertNewPeople(peoples = peoples)
            return Result.success(Unit)
        }catch (
            e : Exception
        ){
            Log.e("Error at addPeopleRepo" , e.message.toString())
           return Result.failure(e)
        }
    }

    suspend fun takePeopleInformation(peopleId: Int) : Result<Peoples> {
        try {
            val people =  peoplesDao.getPeopleById(peopleId)
            return Result.success(people)
        }catch (
            e: Exception
        ){
            return Result.failure(e)
        }
    }
}