package com.cerentekin.bitcointracker.data.remote

import com.cerentekin.bitcointracker.data.model.Coin
import com.cerentekin.bitcointracker.data.model.CoinDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CoinService {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd"
    ): List<Coin>

    @GET("coins/{id}")
    suspend fun getCoinById(
        @Path("id") id: String
    ): CoinDetail

}
