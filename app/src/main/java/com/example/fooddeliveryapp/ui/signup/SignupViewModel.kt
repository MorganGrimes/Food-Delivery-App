package com.example.fooddeliveryapp.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.remote.dto.AuthRequest
import com.example.fooddeliveryapp.data.remote.dto.AuthResponse
import com.example.fooddeliveryapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class SignupViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _signupResult = MutableStateFlow<Response<AuthResponse>?>(null)
    val signupResult: StateFlow<Response<AuthResponse>?> = _signupResult

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            val request = AuthRequest(name, email, password)
            val response = repository.register(request)
            _signupResult.value = response
        }
    }
}