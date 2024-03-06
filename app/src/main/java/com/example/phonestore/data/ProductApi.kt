package com.example.phonestore.data

import com.example.phonestore.data.retrofit.Products
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): Products

    @GET("products?limit=20&")
    suspend fun getProducts(@Query("skip")id: Int): Products

}