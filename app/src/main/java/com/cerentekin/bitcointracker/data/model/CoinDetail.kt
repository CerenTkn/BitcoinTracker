package com.cerentekin.bitcointracker.data.model

import com.google.gson.annotations.SerializedName

data class CoinDetail(
    val id: String,
    val symbol: String,
    val name: String,
    @SerializedName("hashing_algorithm")
    val hashingAlgorithm: String?,
    val description: Description,
    val image: Image,
    @SerializedName("market_data")
    val marketData: MarketData
)