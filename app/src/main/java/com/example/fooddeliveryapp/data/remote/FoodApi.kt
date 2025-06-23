package com.example.fooddeliveryapp.data.remote

import com.example.fooddeliveryapp.data.remote.dto.AuthRequest
import com.example.fooddeliveryapp.data.remote.dto.AuthResponse
import com.example.fooddeliveryapp.data.remote.dto.CategoriesResponse
import com.example.fooddeliveryapp.data.remote.dto.restaurant.CouponResponse
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

interface CouponApi {
    @GET("api/coupons")
    suspend fun getCoupons(): Response<CouponResponse>
}

data class PaymentVerificationRequest(
    val paymentMethod: String,
    val amount: Double,
    val cardId: Int?
)

data class PaymentVerificationResponse(
    val success: Boolean,
    val message: String
)

interface PaymentApi {
    @POST("api/payment/verify")
    suspend fun verifyPayment(
        @Body request: PaymentVerificationRequest
    ): Response<PaymentVerificationResponse>
}