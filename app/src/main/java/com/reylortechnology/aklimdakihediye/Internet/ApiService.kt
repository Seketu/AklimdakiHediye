package com.reylortechnology.aklimdakihediye.Internet

import io.ktor.client.HttpClient
import javax.inject.Inject

class ApiService
    @Inject constructor(
       val client: HttpClient
    ){

}