package com.example.aklimdakihediye.Internet

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

inline fun <reified T> safeApiCall(crossinline action : suspend () -> HttpResponse) : Flow<ApiResponse<T>> {
    return flow {
        action().apply {
            if (status.isSuccess()){
                val x = body<T>()
                emit(ApiResponse.Succes(x))
            }else{
                emit(ApiResponse.Error("${status.value} - ${status.description}"))
            }
        }
    }.catch { cause : Throwable ->
        emit(ApiResponse.Error(cause.message ?: cause.toString()))
    }.flowOn(Dispatchers.IO)
}