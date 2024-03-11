package com.example.phonestore.data.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    @GET("products?limit=20&")
    suspend fun getProducts(@Query("skip")id: Int): Products

    @GET("products/search")
    suspend fun getProductsByName(@Query("q")name: String): Products

}