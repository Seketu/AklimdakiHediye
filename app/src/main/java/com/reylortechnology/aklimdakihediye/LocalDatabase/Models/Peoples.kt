package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import java.time.LocalDate

@Entity("peoples")
data class Peoples(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("people_id")
    val peopleId : Int = 0,
    @ColumnInfo("name")
    val peopleName : String,
    @ColumnInfo("relationship")
    val relationship : TypeRelationship,
    @ColumnInfo("birthday")
    val birthday : LocalDate,
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
