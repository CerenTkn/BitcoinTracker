package com.cerentekin.bitcointracker.VM

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerentekin.bitcointracker.data.model.Coin
import com.cerentekin.bitcointracker.data.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinVM @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    val coinList = MutableLiveData<List<Coin>>()
    val error = MutableLiveData<String?>()

    fun fetchCoins() {
        viewModelScope.launch {
            try {
                val coins = repository.getCoinList()
                coinList.postValue(coins)
            } catch (e: Exception) {
                error.postValue(e.message ?: "Unknown error")
            }
        }
    }
}

