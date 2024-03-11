package com.example.phonestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.phonestore.adapters.ProductAdapter
import com.example.phonestore.data.retrofit.ProductApi
import com.example.phonestore.databinding.ActivityMainBinding
import com.example.phonestore.viewmodels.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    lateinit var binding: ActivityMainBinding

    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        adapter = ProductAdapter(this)
        binding.rvProducts.adapter = adapter

        val productApi = mainViewModel.getApi()

        //setList(productApi, 0) //1..20
        setList(productApi, mainViewModel.pageNumber)
        Log.i("createModel", mainViewModel.pageNumber.toString())



        try{
            binding.btnNextPage.setOnClickListener{
                mainViewModel.clickNextPage()
                mainViewModel.listProducts.observe(this) {
                    Log.i("pageNextNumberModel", mainViewModel.pageNumber.toString())
                    adapter.submitList(it)
                }
            }
        }catch (e : Exception){
            Log.i("Button NextPage Exception", e.toString())
        }

        try{
            binding.btnPrevPage.setOnClickListener {
                mainViewModel.clickPrevPage()
                mainViewModel.listProducts.observe(this) {
                    Log.i("pagePrevNumberModel", mainViewModel.pageNumber.toString())
                    adapter.submitList(it)
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