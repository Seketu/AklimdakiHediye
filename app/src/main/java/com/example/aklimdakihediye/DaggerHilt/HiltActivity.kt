package com.example.aklimdakihediye.DaggerHilt

import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.LocalDatabase
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.Repo.MainRepo
import com.example.aklimdakihediye.Repo.SavedVariableRepo
import com.example.aklimdakihediye.Repo.UserSettingsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import javax.inject.Singleton

@HiltAndroidApp
class MyApp : Application()

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LocalDatabase {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
            CREATE TABLE IF NOT EXISTS saved_gifts (
                giftId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                gift_name TEXT NOT NULL,
                gift_description TEXT NOT NULL,
                gift_url TEXT NOT NULL
            )
        """.trimIndent())
            }
        }

        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "app_database"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideUserDao(database: LocalDatabase): UserInformationDao {
        return database.userInformationDao()
    }

    @Provides
    fun provideGiftsDao(database: LocalDatabase): SavedGiftDao {
        return database.savedGiftsDao()
    }

    @Provides
    fun provideSavedRepository(
        giftDao : SavedGiftDao
    ) : SavedVariableRepo {
        return SavedVariableRepo(giftDao)
    }

    @Provides
    fun provideSettingsRepository(
        giftDao : SavedGiftDao,
        userDao : UserInformationDao,
    ) : UserSettingsRepo{
        return (UserSettingsRepo(userDao,giftDao))
    }

    @Provides
    fun provideUserRepository(savedVariableDao: SavedVariableRepo,userDao: UserInformationDao,client: HttpClient): MainRepo {
        return MainRepo(userDao,savedVariableDao,client)
    }
}
