package com.cerentekin.bitcointracker.VM

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cerentekin.bitcointracker.data.model.Coin
import com.cerentekin.bitcointracker.data.model.CoinDetail
import com.cerentekin.bitcointracker.data.repository.CoinRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinVM @Inject constructor(
    private val repository: CoinRepository
) : ViewModel() {

    val coinList = MutableLiveData<List<Coin>>()
    val error = MutableLiveData<String?>()
    private val _coinDetail = MutableLiveData<CoinDetail>()
    val coinDetail: LiveData<CoinDetail> = _coinDetail

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

    fun getCoinDetail(id: String) {
        viewModelScope.launch {

            try {
                val result = repository.getCoinById(id)
                _coinDetail.postValue(result)
                Log.d("CoinVM", "Response JSON: ${Gson().toJson(result)}")

            } catch (e: Exception) {
                Log.e("CoinVM", "Coin detail error: ${e.localizedMessage}")
            }
        }
    }
}

