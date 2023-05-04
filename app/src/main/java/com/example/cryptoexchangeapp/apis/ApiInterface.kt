package com.example.cryptoexchangeapp.apis

import com.example.cryptoexchangeapp.models.CoinDetails
import com.example.cryptoexchangeapp.models.CurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("data-api/v3/cryptocurrency/listing?start=1&limit=500")
    suspend fun getMarketData() : Response<CurrencyModel>


    @GET("coin/{uuid}")
    suspend fun getCoinDetail(
        @Path("uuid") uuid: String,
        @Query("timePeriod") timePeriod: String
    ): Response<CoinDetails>


}