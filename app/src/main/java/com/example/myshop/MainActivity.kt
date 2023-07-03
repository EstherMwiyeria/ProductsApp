package com.example.myshop

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myshop.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    override fun onResume() {
        super.onResume()
        fetchProducts()
    }
    fun fetchProducts(){
        var apiClient = ApiClient.buildClient(ApiInterface::class.java)
        var request = apiClient.getProducts()
        request.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if (response.isSuccessful) {
                    val products = response.body()?.products ?: emptyList()
                    Toast.makeText(
                        baseContext,
                        "fetched ${products.size} products",
                        Toast.LENGTH_LONG
                    ).show()
                    binding.rvProducts.layoutManager= LinearLayoutManager(this@MainActivity)
                    binding.rvProducts.adapter = ProductAdapter(products)
                } else {
                    val error = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(baseContext, error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_LONG).show()
            }
        })}

}
