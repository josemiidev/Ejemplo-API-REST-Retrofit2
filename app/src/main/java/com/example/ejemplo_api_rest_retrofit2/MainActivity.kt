package com.example.ejemplo_api_rest_retrofit2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ejemplo_api_rest_retrofit2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: DogAdapter
    private val dosImages = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
        binding.svBusqueda.setOnQueryTextListener(this)
    }

    private fun initRecyclerView(){
        adapter = DogAdapter(dosImages)
        binding.rvLista.layoutManager = LinearLayoutManager(this)
        binding.rvLista.adapter = adapter
    }
    private fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://dog.ceo/api/breed/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchByName(query: String){
        CoroutineScope(Dispatchers.IO).launch {
            val call:Response<DogResponse> = getRetrofit().create(APIService::class.java).getDogsByBreeds("$query/images")
            val perros: DogResponse? = call.body()
            runOnUiThread{
                if(call.isSuccessful){
                    val images = perros?.imagen ?: emptyList()
                    dosImages.clear()
                    dosImages.addAll(images)
                    adapter.notifyDataSetChanged()
                }else{
                    showError()
                }
            }
        }
    }
    private fun showError(){
        Toast.makeText(this,"Error de carga",Toast.LENGTH_LONG).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(!query.isNullOrEmpty()){
            searchByName(query.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}