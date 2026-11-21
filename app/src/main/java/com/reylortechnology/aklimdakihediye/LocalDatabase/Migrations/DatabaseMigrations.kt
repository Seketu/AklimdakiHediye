package com.reylortechnology.aklimdakihediye.LocalDatabase.Migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// Hatırlatmalara bildirim zamanı eklendi; mevcut kayıtlar korunur.
val MIGRATION_11_12 = object : Migration(11, 12) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notifications ADD COLUMN notify_at INTEGER NOT NULL DEFAULT 0")
    }
}
