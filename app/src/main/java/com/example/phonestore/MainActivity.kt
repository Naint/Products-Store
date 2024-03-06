package com.example.phonestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.example.phonestore.adapters.ProductAdapter
import com.example.phonestore.data.ProductApi
import com.example.phonestore.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProductAdapter(this)
        binding.rvProducts.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val productApi = retrofit.create(ProductApi::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            val list = productApi.getAllProducts()
                runOnUiThread{
                    binding.apply {
                        adapter.submitList(list.products)
                    }
                }
        }
    }
}