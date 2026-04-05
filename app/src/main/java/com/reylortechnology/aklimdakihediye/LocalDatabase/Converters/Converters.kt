package com.reylortechnology.aklimdakihediye.LocalDatabase.Converters

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import com.reylortechnology.aklimdakihediye.ObserverClasses.ZodiacStatus
import com.reylortechnology.aklimdakihediye.models.Enums.CharacterTrait
import com.reylortechnology.aklimdakihediye.models.Enums.Genders
import com.reylortechnology.aklimdakihediye.models.Enums.Hobbies
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

    @TypeConverter
    fun fromHobbies(hobbies: Hobbies?): Int? {
        // Enum'ı veritabanına kaydetmek için id değerine dönüştürüyoruz
        return hobbies?.id
    }

    @TypeConverter
    fun toHobbies(id: Int?): Hobbies? {
        // Veritabanından gelen id değeriyle eşleşen Enum'ı buluyoruz
        // Not: Kotlin 1.9 öncesi bir sürüm kullanıyorsanız Hobbies.entries yerine Hobbies.values() kullanın.
        return id?.let { currentId ->
            Hobbies.entries.find { it.id == currentId }
        }
    }

    // --- BİRDEN FAZLA HOBİ (LİSTE) İÇİN CONVERTER ---

    @TypeConverter
    fun fromHobbiesList(hobbies: List<Hobbies>?): String? {
        // Hobi listesini veritabanına kaydetmek için id'leri virgülle ayırarak String'e çeviriyoruz. (Örn: "0,4,12")
        return hobbies?.joinToString(",") { it.id.toString() }
    }

    @TypeConverter
    fun toHobbiesList(data: String?): List<Hobbies>? {
        // Veritabanından gelen virgüllü String'i tekrar List<Hobbies> formatına çeviriyoruz.
        if (data.isNullOrEmpty()) return emptyList()

        return data.split(",")
            .mapNotNull { it.toIntOrNull() } // String'leri Int'e çevir (Hatalı verileri atla)
            .mapNotNull { id -> Hobbies.entries.find { it.id == id } } // İlgili ID'ye sahip hobiyi bul
    }

    @TypeConverter
    fun fromCharacterTrait(trait: CharacterTrait?): Int? {
        return trait?.id
    }

    @TypeConverter
    fun toCharacterTrait(id: Int?): CharacterTrait? {
        return id?.let { traitId ->
            CharacterTrait.entries.find { it.id == traitId }
        }
    }

    // Eğer kişinin birden fazla karakter özelliği olacaksa (List<CharacterTrait> için)
    @TypeConverter
    fun fromCharacterTraitList(traits: List<CharacterTrait>?): String? {
        // ID'leri aralarına virgül koyarak String'e çevirir (Örn: "1,5,12")
        return traits?.joinToString(separator = ",") { it.id.toString() }
    }

    @TypeConverter
    fun toCharacterTraitList(data: String?): List<CharacterTrait>? {
        if (data.isNullOrEmpty()) return emptyList()
        // Virgülle ayrılmış String'i tekrar List<CharacterTrait>'e dönüştürür
        return data.split(",").mapNotNull { idString ->
            val id = idString.toIntOrNull()
            CharacterTrait.entries.find { it.id == id }
        }
    }

    @TypeConverter
    fun toGender(value : String) : Genders {
        return Genders.valueOf(value)
    }

    @TypeConverter
    fun fromGender(gender : Genders) : String {
        return gender.name
    }

    @TypeConverter
    fun fromZodiacStatus(status: ZodiacStatus?): String? {
        return status?.name
    }

    // Veritabanından okunan String'i tekrar Enum'a çevirir
    @TypeConverter
    fun toZodiacStatus(name: String?): ZodiacStatus? {
        if (name == null) return null

        return try {
            ZodiacStatus.valueOf(name)
        } catch (e: IllegalArgumentException) {
            // Eğer veritabanında karşılığı olmayan veya silinmiş bir enum değeri varsa
            // uygulamanın çökmesini önlemek için varsayılan bir değer döndürülür.
            ZodiacStatus.None
        }
    }

}