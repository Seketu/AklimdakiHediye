package com.reylortechnology.aklimdakihediye.DaggerHilt

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.reylortechnology.aklimdakihediye.LocalDatabase.Migrations.MIGRATION_11_12
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.NotificationsDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.PeoplesDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.SavedGiftDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.Dao.UserInformationDao
import com.reylortechnology.aklimdakihediye.LocalDatabase.LocalDatabase
import com.reylortechnology.aklimdakihediye.Repo.MainRepo
import com.reylortechnology.aklimdakihediye.Repo.NotificationsRepo
import com.reylortechnology.aklimdakihediye.Repo.PeopleRepo
import com.reylortechnology.aklimdakihediye.Repo.SavedVariableRepo
import com.reylortechnology.aklimdakihediye.Repo.UserSettingsRepo
import com.reylortechnology.aklimdakihediye.Services.DuckDuckGoSearch
import com.reylortechnology.aklimdakihediye.Services.ImageSearchService
import com.reylortechnology.aklimdakihediye.Services.ProductImageSelector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltAndroidApp
class MyApp : Application() {

    @Inject
    lateinit var notificationsRepo: NotificationsRepo

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Alarmlar yeniden başlatma / zorla durdurma / yedekten geri yüklemede silinir; her açılışta tekrar kur
        appScope.launch {
            runCatching { notificationsRepo.rescheduleAll() }
                .onFailure { Log.e("MyApp", "Hatırlatmalar yeniden kurulamadı", it) }
        }
    }
}

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
            .addMigrations(MIGRATION_11_12)
            .fallbackToDestructiveMigration(false)
            .build()
    }


     @Provides
     fun provideCalculatorSpecialDays() : com.reylortechnology.aklimdakihediye.Services.CalculatorSpecialDays{
         return com.reylortechnology.aklimdakihediye.Services.CalculatorSpecialDays()
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
        return UserSettingsRepo(userDao,giftDao)
    }

    @Provides
    fun providePeopleDao(database: LocalDatabase) : PeoplesDao{
        return database.peoplesDao()
    }

    @Provides
    fun provideDuckDuckGoSearch(client: HttpClient) : DuckDuckGoSearch{
        return DuckDuckGoSearch(client)
    }

    @Provides
    fun provideProductImageSelector() : ProductImageSelector {
        return ProductImageSelector()
    }

    @Provides
    @Singleton // İstek sınırlayıcı ve önbellek tek örnekte tutulmalı
    fun provideImageSearchService(client: HttpClient, selector: ProductImageSelector) : ImageSearchService {
        return ImageSearchService(client, selector)
    }

    @Provides
    fun provideUserRepository(
        savedVariableDao: SavedVariableRepo,
        userDao: UserInformationDao,
        client: HttpClient,
        providePeopleDao: PeoplesDao,
        duckGoSearch: DuckDuckGoSearch,
        imageSearchService: ImageSearchService
    ): MainRepo {
        return MainRepo(
            userInformationDao = userDao,
            giftInformationDao = savedVariableDao,
            peopleInformationDao = providePeopleDao,
            client = client, duckSearch = duckGoSearch,
            bingSearchService = imageSearchService
        )
    }

    @Provides
    fun providePeopleRope(
        peopleDao : PeoplesDao
    ): PeopleRepo{
        return PeopleRepo(peoplesDao = peopleDao)
    }

    @Provides
    fun provideNotificationsRepo(
        @ApplicationContext context: Context,
        notificationDao: NotificationsDao
    ): NotificationsRepo{
        return NotificationsRepo(context, notificationDao)
    }

}
