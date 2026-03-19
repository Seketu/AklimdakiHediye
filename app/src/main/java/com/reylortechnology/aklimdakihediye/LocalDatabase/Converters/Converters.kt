package com.reylortechnology.aklimdakihediye.LocalDatabase.Converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.reylortechnology.aklimdakihediye.models.Enums.TypeRelationship

class Converters {
    @TypeConverter
    fun fromColor(color: Color): Long {
        return color.value.toLong()
    }

    @TypeConverter
    fun toColor(value: Long): Color {
        return Color(value.toULong())
    }

    @TypeConverter
    fun fromTypeRelationship(relationship: TypeRelationship): String {
        return relationship.name
    }

    @TypeConverter
    fun toTypeRelationship(value: String): TypeRelationship {
        return TypeRelationship.valueOf(value)
    }

    @TypeConverter
    fun fromLocalDate(localDate: java.time.LocalDate): String {
        return localDate.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): java.time.LocalDate {
        return java.time.LocalDate.parse(value)
    }
}