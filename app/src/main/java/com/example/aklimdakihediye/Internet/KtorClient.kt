package com.example.aklimdakihediye.Internet

import android.util.Log
import com.example.aklimdakihediye.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object KtorClient {

    @Provides
    @Singleton
    fun provideHttpClient (): HttpClient = HttpClient(Android){


        install(ContentNegotiation){
            json(
                Json{
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = false
                }
            )
        }

        install(Logging){
            logger = object : Logger{
                override fun log(message: String) {
                    Log.d("KTOR" , message)
                }
            }

            level = LogLevel.BODY
        }
        engine {
            connectTimeout = 30_000
            socketTimeout = 30_000
        }
    }

}