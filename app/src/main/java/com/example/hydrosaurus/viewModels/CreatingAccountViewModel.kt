package com.example.hydrosaurus.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CreatingAccountViewModel: ViewModel() {
    private val _createState = MutableStateFlow<CreateState>(CreateState.NOTCREATING)
    val createState: StateFlow<CreateState> = _createState.asStateFlow()
    private val auth = FirebaseAuth.getInstance()

    fun goToCreateUserWithEmailAndPassword(){
        _createState.value = CreateState.CREATING
    }

    fun goToSignInUserWithEmailAndPassword(){
        _createState.value = CreateState.NOTCREATING
    }

    fun createUserWithEmailAndPassword(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _createState.value = CreateState.NOTCREATING
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                } else {
                    Log.e("myTags", "createUserWithEmailAndPassword email: $email, password: $password went wrong")
                }
            }

    }

}

sealed class CreateState {
    object NOTCREATING : CreateState()
    object CREATING : CreateState()
}