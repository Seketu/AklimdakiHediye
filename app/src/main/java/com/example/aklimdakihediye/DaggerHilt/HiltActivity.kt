package com.example.aklimdakihediye.DaggerHilt

import android.app.Application
import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import com.example.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.example.aklimdakihediye.LocalDatabase.LocalDatabase
import com.example.aklimdakihediye.NavController.LocalNavController
import com.example.aklimdakihediye.Repo.MainRepo
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
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideUserDao(database: LocalDatabase): UserInformationDao {
        return database.userInformationDao()
    }

    @Provides
    fun provideUserRepository(userDao: UserInformationDao,client: HttpClient): MainRepo {
        return MainRepo(userDao,client)
    }
}
