package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship
import java.time.LocalDate

@Entity("peoples")
data class Peoples(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("people_id")
    val peopleId: Int = 0,
    @ColumnInfo("name")
    var peopleName: String,
    @ColumnInfo("relationship")
    val relationship: TypeRelationship,
    @ColumnInfo("birthday")
    val birthday: LocalDate,
    @ColumnInfo("age")
    val age: Int,
    @ColumnInfo("zodiac")
    val zodiac: ZodiacStatus,
    @ColumnInfo("hobbies")
    val hobbies: List<Hobbies>,
    @ColumnInfo("best_side")
    val bestSide: String,
    @ColumnInfo("character")
    val character: List<CharacterTrait>,
    @ColumnInfo("job")
    val job: String,
    @ColumnInfo("image")
    val image: Int,
    @ColumnInfo("color")
    val color: Color,
    @ColumnInfo("gender")
    val gender: Genders
)
