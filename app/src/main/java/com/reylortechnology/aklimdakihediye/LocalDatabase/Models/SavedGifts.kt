package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "saved_gifts")
data class SavedGifts(
    @PrimaryKey(autoGenerate = true)
    val giftId : Int = 0,
    @ColumnInfo("gift_name")
    val giftName : String,
    @ColumnInfo("gift_description")
    val giftDescription : String,
    @ColumnInfo("gift_url")
    val giftUrl : String
)