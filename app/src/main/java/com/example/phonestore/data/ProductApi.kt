package com.example.phonestore.data

import com.example.phonestore.data.retrofit.Products
import retrofit2.http.GET

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): Products
}