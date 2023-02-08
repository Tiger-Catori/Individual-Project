package com.example.cryptoexchangeapp.apis

import com.example.cryptoexchangeapp.models.CurrencyModel
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getMarketData() : Response<CurrencyModel>


}