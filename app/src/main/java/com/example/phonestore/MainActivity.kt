package com.example.phonestore

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AlertDialog
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

        init()
    }

    private fun init(){
        if(isOnline(this)){
            val productApi = mainViewModel.getApi()
            setList(productApi, mainViewModel.pageNumber) //1..20(0)
            onClickListeners()
            mainViewModel.listProducts.observe(this) {
                adapter.submitList(it)
            }
        }
        else{
            createDialogRestart()
        }
    }

    private fun onClickListeners(){
        try{
            binding.btnNextPage.setOnClickListener{
                if(isOnline(this)){
                    mainViewModel.clickNextPage()
                }
                else{
                    createDialogRestart()
                }
            }
        }catch (e : Exception){
            Log.i("Button NextPage Exception", e.toString())
        }

        try{
            binding.btnPrevPage.setOnClickListener {
                if(isOnline(this)){
                    mainViewModel.clickPrevPage()
                }
                else{
                    createDialogRestart()
                }
            }
        }catch (e: Exception){
            Log.i("Button PrevPage Exception", e.toString())
        }

        try{
            if(isOnline(this)){
                binding.svSearchProduct.setOnQueryTextListener(object: OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean {
                        if (p0 != null) {
                            mainViewModel.searchProduct(p0)
                        }
                        return true
                    }
                    override fun onQueryTextChange(p0: String?): Boolean {
                        return true
                    }
                })
            }
            else{
                createDialogRestart()
            }
        }catch (e: Exception){
            Log.i("SearchView Exception", e.toString())
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

    private fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    private fun createDialogRestart(){
        val artDialogBuilder = AlertDialog.Builder(this@MainActivity)
        artDialogBuilder.setMessage("Мы не можем получить данные. Проверьте интернет соединение")
        artDialogBuilder.setCancelable(false)

        artDialogBuilder.setPositiveButton("Соединение в норме."){_,_ ->
            init()
        }

        val alertDialogBox = artDialogBuilder.create()
        alertDialogBox.show()


    }

}