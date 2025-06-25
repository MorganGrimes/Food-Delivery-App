package com.example.fooddeliveryapp.data.remote.dto

data class CategoriesResponse(
    val categories: List<CategoryDto>
)

data class CategoryDto(
    val name: String,
)