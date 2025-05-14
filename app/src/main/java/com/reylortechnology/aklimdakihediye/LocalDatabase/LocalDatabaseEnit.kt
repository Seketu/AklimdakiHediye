package com.reylortechnology.aklimdakihediye.LocalDatabase

import android.content.Context
import androidx.room.Room

object LocalDatabaseEnit {
        @Volatile
        private var instance : LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase{
            return instance ?: synchronized(this){
                val newInstance = Room.databaseBuilder(
                    context =  context.applicationContext,
                    klass = LocalDatabase::class.java,
                    "Database"
                ).fallbackToDestructiveMigration()
                    .build()
                instance = newInstance
                newInstance
            }
        }
}