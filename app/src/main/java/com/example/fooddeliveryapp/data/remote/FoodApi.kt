package com.example.fooddeliveryapp.data.remote

import com.example.fooddeliveryapp.data.remote.dto.AuthRequest
import com.example.fooddeliveryapp.data.remote.dto.AuthResponse
import com.example.fooddeliveryapp.data.remote.dto.CategoriesResponse
import com.example.fooddeliveryapp.data.remote.dto.restaurant.RestaurantsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: AuthRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: AuthRequest): Response<AuthResponse>
}

interface CategoryApi {
    @GET("api/foodCategories")
    suspend fun getCategories(): CategoriesResponse
}

interface RestaurantApi {
    @GET("api/restaurants")
    suspend fun getRestaurants(): Response<RestaurantsResponse>
}