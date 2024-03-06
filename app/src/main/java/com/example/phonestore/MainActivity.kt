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
import retrofit2.create
import java.lang.NullPointerException


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ProductAdapter(this)
        binding.rvProducts.adapter = adapter

        val productApi = Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductApi::class.java)

        setList(productApi, 0) //1..20
        var pageNumber = 1
        val pageKeys = mapOf(1 to 0, 2 to 20, 3 to 40, 4 to 60, 5 to 80, 6 to 100, 7 to 120)

        try{
            binding.btnNextPage.setOnClickListener{
                var buff = pageNumber
                pageNumber += 1
                if(pageNumber > 6)
                    pageNumber = buff
                Log.i("NextPate", pageNumber.toString())
                CoroutineScope(Dispatchers.IO).launch {

                    val list = productApi.getProducts(pageKeys[pageNumber]!!)
                    runOnUiThread{
                        binding.apply {
                            if(list.products.isNotEmpty())
                                adapter.submitList(list.products)
                            else
                                pageNumber = buff
                        }
                    }

                }
            }
        }catch (e : Exception){
            Log.i("Button NextPage Exception", e.toString())
        }


        try{
            binding.btnPrevPage.setOnClickListener {
                var buff = pageNumber
                if(pageNumber > 1)
                    pageNumber -= 1
                Log.i("PrevPage", pageNumber.toString())
                CoroutineScope(Dispatchers.IO).launch{
                    val list = productApi.getProducts(pageKeys.get(pageNumber)!!)
                    runOnUiThread {
                        binding.apply {
                            if(list.products.isNotEmpty())
                                adapter.submitList(list.products)
                            else
                                pageNumber = buff
                        }
                    }

                }
            }
        }catch (e: Exception){
            Log.i("Button PrevPage Exception", e.toString())
        }
    }

    private fun setList(productApi: ProductApi, number: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val list = productApi.getProducts(number)
            runOnUiThread{
                binding.apply {
                    adapter.submitList(list.products)
                }
            }
        }
    }

}