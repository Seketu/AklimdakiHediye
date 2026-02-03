package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_information")
class LocalUserInformation(
    @PrimaryKey(autoGenerate = true)
    val uid : Int = 0,
    @ColumnInfo("user_name")
    val name : String,
    @ColumnInfo("user_old")
    val old : Int,
    @ColumnInfo("user_best_side")
    val bestSide : String,
    @ColumnInfo("user_hobbies")
    val hobbies : String,
    @ColumnInfo("user_zodiac")
    val zodiac : String,
    @ColumnInfo("user_caracter")
    val character : String
)