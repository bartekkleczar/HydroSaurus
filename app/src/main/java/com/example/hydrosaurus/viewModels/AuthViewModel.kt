package com.example.hydrosaurus.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.UNAUTHENTICATED)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val auth = FirebaseAuth.getInstance()

    init {
        auth.addAuthStateListener {
            if (it.currentUser != null) {
                _authState.value = AuthState.AUTHENTICATED
            } else {
                _authState.value = AuthState.UNAUTHENTICATED
            }
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.AUTHENTICATED
                } else {
                    Log.e("myTags", "signInWithEmailAndPassword email: $email, password: $password went wrong")
                }
            }
    }

    fun createUserWithEmailAndPassword(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signInWithEmailAndPassword(email, password)
                } else {
                    Log.e("myTags", "createUserWithEmailAndPassword email: $email, password: $password went wrong")
                }
            }
    }


    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.UNAUTHENTICATED
    }


}

sealed class AuthState {
    object UNAUTHENTICATED : AuthState()
    object AUTHENTICATED : AuthState()
}