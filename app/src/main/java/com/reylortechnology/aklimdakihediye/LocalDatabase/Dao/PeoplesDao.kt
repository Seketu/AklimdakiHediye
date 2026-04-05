package com.reylortechnology.aklimdakihediye.LocalDatabase.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.Peoples
import kotlinx.coroutines.flow.Flow

@Dao
interface PeoplesDao {

    @Query("SELECT * FROM peoples")
    fun getAllPeoples(): Flow<List<Peoples>>

    @Insert
    suspend fun insertNewPeople(peoples: Peoples)

    @Delete
    suspend fun deletePeople(peoples: Peoples)

    @Update
    suspend fun updatePeople(peoples: Peoples)

    @Query("SELECT * FROM peoples WHERE people_id = :peopleId")
    suspend fun getPeopleById(peopleId: Int) : Peoples
}