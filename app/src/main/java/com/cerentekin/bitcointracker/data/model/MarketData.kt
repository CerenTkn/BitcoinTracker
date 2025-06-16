package com.cerentekin.bitcointracker.data.model

import com.google.gson.annotations.SerializedName

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: Map<String, Double>,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?
)