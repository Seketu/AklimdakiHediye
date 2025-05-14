package com.reylortechnology.aklimdakihediye.DaggerHilt

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.LocalDatabase
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import com.reylortechnology.aklimdakihediye.Repo.NotificationsRepo
import com.reylortechnology.aklimdakihediye.Repo.SavedVariableRepo
import com.reylortechnology.aklimdakihediye.Repo.UserSettingsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "app_database"
        )
            .fallbackToDestructiveMigration()
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
    fun provideNotificationsDao(database: LocalDatabase): NotificationsDao {
        return database.notificationsDao()
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

    @Provides
    fun provideNotificationsRepo(notificationDao: NotificationsDao): NotificationsRepo{
        return NotificationsRepo(notificationDao)
    }

}
