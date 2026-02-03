package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("last_gifts",
    foreignKeys = [
        ForeignKey(
            entity = Peoples :: class,
            parentColumns = ["people_id"],
            childColumns = ["people_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class LastGifts(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("gift_id")
    val giftId : Int = 0,
    @ColumnInfo("people_id")
    val userId : Int,
    @ColumnInfo("gift_name")
    val giftName : String
)
