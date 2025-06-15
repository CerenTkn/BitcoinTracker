package com.cerentekin.bitcointracker.VM

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cerentekin.bitcointracker.data.model.Coin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FavouritesVM @Inject constructor() : ViewModel() {
    private val _favouriteCoins = MutableLiveData<List<Coin>>()
    val favouriteCoins: LiveData<List<Coin>> get() = _favouriteCoins

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun fetchFavourites() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("favourites")
            .get()
            .addOnSuccessListener { snapshot ->
                val coinList = snapshot.toObjects(Coin::class.java)
                _favouriteCoins.value = coinList
            }
            .addOnFailureListener {
                _favouriteCoins.value = emptyList()
            }
    }

}