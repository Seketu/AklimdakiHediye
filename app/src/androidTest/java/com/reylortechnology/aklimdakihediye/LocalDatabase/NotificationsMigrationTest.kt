package com.reylortechnology.aklimdakihediye.LocalDatabase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.reylortechnology.aklimdakihediye.LocalDatabase.Migrations.MIGRATION_11_12
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * v11 veritabanını (cihazdaki gerçek şemadan alındı, notifications'ta notify_at yok) elle kurar,
 * Room'u MIGRATION_11_12 ile açar. Room şema doğrulaması başarısız olursa test patlar.
 */
@RunWith(AndroidJUnit4::class)
class NotificationsMigrationTest {

    private val dbName = "migration_test_db"
    private lateinit var context: Context

    private val v11Schema = listOf(
        "CREATE TABLE `user_information` (`uid` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_name` TEXT NOT NULL, `user_old` INTEGER NOT NULL, `user_best_side` TEXT NOT NULL, `user_hobbies` TEXT NOT NULL, `user_zodiac` TEXT NOT NULL, `user_caracter` TEXT NOT NULL)",
        "CREATE TABLE `saved_gifts` (`giftId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `gift_name` TEXT NOT NULL, `gift_url` TEXT NOT NULL, `gift_image` TEXT NOT NULL)",
        "CREATE TABLE `notifications` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `for_who` TEXT NOT NULL, `event` TEXT NOT NULL, `date` INTEGER NOT NULL, `releationship` TEXT NOT NULL, `message` TEXT NOT NULL)",
        "CREATE TABLE `peoples` (`people_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `relationship` TEXT NOT NULL, `birthday` TEXT NOT NULL, `age` INTEGER NOT NULL, `zodiac` TEXT NOT NULL, `hobbies` TEXT NOT NULL, `best_side` TEXT NOT NULL, `character` TEXT NOT NULL, `job` TEXT NOT NULL, `image` INTEGER NOT NULL, `color` INTEGER NOT NULL, `gender` TEXT NOT NULL)",
        "CREATE TABLE `last_gifts` (`gift_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `people_id` INTEGER NOT NULL, `gift_name` TEXT NOT NULL, FOREIGN KEY(`people_id`) REFERENCES `peoples`(`people_id`) ON UPDATE CASCADE ON DELETE CASCADE )"
    )

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        context.deleteDatabase(dbName)
    }

    @After
    fun tearDown() {
        context.deleteDatabase(dbName)
    }

    @Test
    fun migrate11To12_keepsRemindersAndOtherData() {
        SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(dbName), null).use { db ->
            v11Schema.forEach(db::execSQL)
            db.execSQL(
                "INSERT INTO notifications (id, for_who, event, date, releationship, message) " +
                    "VALUES (7, 'Ayşe', 'Doğum günü', 1798934400000, 'Annem', 'mesaj')"
            )
            db.execSQL(
                "INSERT INTO peoples (people_id, name, relationship, birthday, age, zodiac, hobbies, best_side, character, job, image, color, gender) " +
                    "VALUES (1, 'Ayşe', 'Annem', '01.01.1970', 56, 'Koç', '', '', '', '', 0, 0, 'Kadın')"
            )
            db.version = 11
        }

        val room = Room.databaseBuilder(context, LocalDatabase::class.java, dbName)
            .addMigrations(MIGRATION_11_12)
            .build()
        try {
            val reminders = runBlocking { room.notificationsDao().getAllNotificationsOnce() }
            assertEquals(1, reminders.size)
            val r = reminders.single()
            assertEquals(7, r.id)
            assertEquals("Ayşe", r.for_who)
            assertEquals("Doğum günü", r.event)
            assertEquals(1798934400000L, r.date)
            assertEquals("Annem", r.releationship)
            assertEquals("mesaj", r.message)
            assertEquals("Eski kayıt notify_at=0 olmalı", 0L, r.notifyAt)

            val sqlDb = room.openHelper.readableDatabase
            assertEquals(12, sqlDb.version)
            sqlDb.query("SELECT COUNT(*) FROM peoples").use {
                it.moveToFirst()
                assertEquals("Kişiler silinmemeli", 1, it.getInt(0))
            }
        } finally {
            room.close()
        }
    }
}
