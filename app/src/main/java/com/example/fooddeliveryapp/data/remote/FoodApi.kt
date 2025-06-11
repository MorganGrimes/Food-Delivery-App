package com.example.fooddeliveryapp.data.remote

import com.example.fooddeliveryapp.data.remote.dto.AuthRequest
import com.example.fooddeliveryapp.data.remote.dto.AuthResponse
import com.example.fooddeliveryapp.data.remote.dto.CategoriesResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}

interface FoodApi {
    @GET("api/food")
    suspend fun getCategories(): CategoriesResponse

    @POST("api/food")
    suspend fun addCategory(@Body category: String): Response<Unit>

    @PUT("api/food")
    suspend fun updateCategory(@Body category: String): Response<Unit>

    @DELETE("api/food")
    suspend fun deleteCategory(@Body category: String): Response<Unit>
}
