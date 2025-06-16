package com.cerentekin.bitcointracker.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
    var id: String = "",
    var symbol: String = "",
    var name: String = "",
    @SerializedName("image")
    var image: String = "",

    @get:PropertyName("current_price")
    @set:PropertyName("current_price")
    @SerializedName("current_price")
    var currentPrice: Double = 0.0,

    @get:PropertyName("market_cap")
    @set:PropertyName("market_cap")
    @SerializedName("market_cap")
    var marketCap: Long = 0L,

    @get:PropertyName("price_change_percentage_24h")
    @set:PropertyName("price_change_percentage_24h")
    @SerializedName("price_change_percentage_24h")
    var priceChangePercentage24h: Double = 0.0
) : Parcelable
