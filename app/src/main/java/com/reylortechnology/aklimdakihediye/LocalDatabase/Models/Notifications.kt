package com.reylortechnology.aklimdakihediye.LocalDatabase.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class Notifications (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "for_who")
    val for_who: String,
    @ColumnInfo(name = "event")
    val event: String,
    @ColumnInfo(name = "date")
    val date : Long,
    @ColumnInfo(name = "releationship")
    val releationship : String,
    @ColumnInfo(name = "message")
    val message : String
)