package com.example.ejemplo_api_rest_retrofit2

import com.google.gson.annotations.SerializedName


data class DogResponse (
    @SerializedName("status") var status: String,
    @SerializedName("message") var imagen: List<String>)