package com.cerentekin.bitcointracker.data.remote

import com.cerentekin.bitcointracker.data.model.Coin
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinService {

    @GET("coins/markets")
    suspend fun getCoins(
        @Query("vs_currency") currency: String = "usd"
    ): List<Coin>
}
