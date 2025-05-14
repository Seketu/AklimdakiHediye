package com.reylortechnology.aklimdakihediye.Internet

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

inline fun <reified T> safeApiCall(crossinline action: suspend () -> HttpResponse): Flow<ApiResponse<T>> {
    return flow {
        val response = action()
        if (response.status.isSuccess()) {
            val contentType = response.headers["Content-Type"] ?: ""

            val parsedBody: T = if (contentType.contains("application/json")) {
                response.body()
            } else {
                val raw = response.bodyAsText()
                Json { ignoreUnknownKeys = true }.decodeFromString(raw)
            }

            emit(ApiResponse.Succes(parsedBody))
        } else {
            emit(ApiResponse.Error("${response.status.value} - ${response.status.description}"))
        }
    }.catch { cause: Throwable ->
        emit(ApiResponse.Error(cause.message ?: cause.toString()))
    }.flowOn(Dispatchers.IO)
}
