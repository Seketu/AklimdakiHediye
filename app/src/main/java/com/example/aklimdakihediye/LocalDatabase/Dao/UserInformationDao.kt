package com.example.aklimdakihediye.LocalDatabase.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.aklimdakihediye.LocalDatabase.Models.LocalUserInformation
import kotlinx.coroutines.flow.Flow

@Dao
interface UserInformationDao {
    @Query("SELECT * FROM user_information")
    fun getLocalInformation() : Flow<List<LocalUserInformation>>

    @Insert
    suspend fun addLocalInformation(localUserInformation: LocalUserInformation)

    @Query("DELETE FROM user_information")
    suspend fun deleteLocalInformation()

}