package com.example.fooddeliveryapp.data.repository

import com.example.fooddeliveryapp.data.remote.RetrofitInstance
import com.example.fooddeliveryapp.data.remote.dto.AuthRequest
import com.example.fooddeliveryapp.data.remote.dto.AuthResponse
import retrofit2.Response

class AuthRepository {
    suspend fun register(request: AuthRequest): Response<AuthResponse> {
        return RetrofitInstance.authApi.register(request)
    }

    suspend fun login(request: AuthRequest): Response<AuthResponse> {
        return RetrofitInstance.authApi.login(request)
    }
}