package com.cerentekin.bitcointracker.data.repository

import com.cerentekin.bitcointracker.data.model.CoinDetail
import com.cerentekin.bitcointracker.data.remote.CoinService
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val api: CoinService
) {
    suspend fun getCoinList() = api.getCoins()

    suspend fun getCoinById(id: String): CoinDetail {
        return api.getCoinById(id)
    }

}
