package com.example.phonestore.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.phonestore.data.Product
import com.example.phonestore.data.retrofit.ProductApi
import com.example.phonestore.data.retrofit.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel(application: Application): AndroidViewModel(application) {

    var pageNumber = 0
    val elementsOnPage = 20

    var listProducts = MutableLiveData<List<Product>>()

    fun getApi(): ProductApi {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)
    }

    fun clickNextPage(){
        val buff = pageNumber
        if(pageNumber == 0)
            pageNumber += 1
        pageNumber += 1
        viewModelScope.launch {
            val list = getApi().getProducts(pageNumber * elementsOnPage - elementsOnPage)
            if(list.products.isNotEmpty()){
                listProducts.value = list.products
            }
            else{
                pageNumber = buff
            }
        }
    }

    fun clickPrevPage(){
        val buff = pageNumber
        if(pageNumber > 1)
            pageNumber -= 1
        viewModelScope.launch {
            val list = getApi().getProducts(pageNumber * elementsOnPage - elementsOnPage)
            if(list.products.isNotEmpty()){
                listProducts.value = list.products
            }
            else{
                pageNumber = buff
            }
        }
    }

}