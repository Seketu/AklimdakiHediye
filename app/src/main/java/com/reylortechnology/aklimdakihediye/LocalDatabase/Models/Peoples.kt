package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("peoples")
data class Peoples(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("people_id")
    val peopleId : Int,
    @ColumnInfo("name")
    val peopleName : String,
    @ColumnInfo("age")
    val age : Int,
    @ColumnInfo("zodiac")
    val zodiac : String,
    @ColumnInfo("hobbies")
    val hobbies : String,
    @ColumnInfo("best_side")
    val bestSide : String,
    @ColumnInfo("character")
    val character : String,
    @ColumnInfo("job")
    val job : String,
    @ColumnInfo("image")
    val image : Int,
    @ColumnInfo("color")
    val color : Color
)
