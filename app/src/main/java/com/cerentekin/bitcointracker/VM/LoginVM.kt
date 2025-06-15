package com.cerentekin.bitcointracker.VM

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth

) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult

    fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loginResult.value = task.isSuccessful
            }
            .addOnFailureListener { exception ->
                Log.e("LoginVM", "Giriş hatası: ${exception.message}", exception)
            }
    }

}
