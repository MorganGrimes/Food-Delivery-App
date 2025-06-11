package com.example.fooddeliveryapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fooddeliveryapp.data.remote.dto.AuthRequest
import com.example.fooddeliveryapp.data.remote.dto.AuthResponse
import com.example.fooddeliveryapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _loginResult = MutableStateFlow<Response<AuthResponse>?>(null)
    val loginResult: StateFlow<Response<AuthResponse>?> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val request = AuthRequest("", email, password)
            val response = repository.login(request)
            _loginResult.value = response
        }
    }
}