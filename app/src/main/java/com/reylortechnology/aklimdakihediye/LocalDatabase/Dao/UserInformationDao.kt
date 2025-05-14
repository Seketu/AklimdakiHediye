package com.reylortechnology.aklimdakihediye.LocalDatabase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.reylortechnology.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInformationDao {
    @Query("SELECT * FROM user_information")
    fun getLocalInformation() : Flow<List<LocalUserInformation>>

    @Insert
    suspend fun addLocalInformation(localUserInformation: LocalUserInformation)

    @Query("DELETE FROM user_information")
    suspend fun deleteLocalInformation()

    @Query("UPDATE user_information SET user_name = :name, user_hobbies = :hobbies,user_caracter = :caracter, user_old = :old, user_zodiac = :zodiac,user_best_side =:bestSide where uid = :guid ")
    suspend fun updateLocalInformation(name : String, hobbies : String, caracter : String, old : Int, zodiac : String, bestSide : String , guid : Int)
}