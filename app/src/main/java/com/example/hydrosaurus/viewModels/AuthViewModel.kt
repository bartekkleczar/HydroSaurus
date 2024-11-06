package com.example.hydrosaurus.viewModels

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.hydrosaurus.contains
import com.google.firebase.auth.ActionCodeSettings
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

    fun signInWithEmailAndPassword(email: String, password: String): Boolean {
        var output = true
        if (email.isEmpty() || !(email.contains(x = '@'))) return false
        if (password.isEmpty() || password.length < 6 || password.length > 4096) return false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.AUTHENTICATED
                    output = true
                } else {
                    Log.e("myTags", "signInWithEmailAndPassword email: $email, password: $password went wrong")
                }
            }
        return output
    }

    fun authenticate(){
        _authState.value = AuthState.AUTHENTICATED
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