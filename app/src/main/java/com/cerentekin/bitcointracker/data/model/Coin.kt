package com.cerentekin.bitcointracker.data.model

import com.google.gson.annotations.SerializedName

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double,
    @SerializedName("market_cap")
    val marketCap: Long,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double
)

