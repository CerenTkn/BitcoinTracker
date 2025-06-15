package com.cerentekin.bitcointracker.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
    var id: String = "",
    var symbol: String = "",
    var name: String = "",
    @SerializedName("image")
    var image: String = "",
    @SerializedName("current_price")
    var currentPrice: Double = 0.0,
    @SerializedName("market_cap")
    var marketCap: Long = 0L,
    @SerializedName("price_change_percentage_24h")
    var priceChangePercentage24h: Double = 0.0
) : Parcelable
