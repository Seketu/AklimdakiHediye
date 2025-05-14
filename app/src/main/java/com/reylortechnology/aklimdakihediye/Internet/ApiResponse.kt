package com.reylortechnology.aklimdakihediye.Internet

sealed class ApiResponse<out T> {
    data class Succes<TYPE>(val body : TYPE) : ApiResponse<TYPE>()

    data class Error(val message : String) : ApiResponse<Nothing>()
}