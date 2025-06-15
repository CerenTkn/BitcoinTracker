package com.cerentekin.bitcointracker.data.repository

import com.cerentekin.bitcointracker.data.remote.CoinService
import javax.inject.Inject

class CoinRepository @Inject constructor(
    private val api: CoinService
) {
    suspend fun getCoinList() = api.getCoins()
}
