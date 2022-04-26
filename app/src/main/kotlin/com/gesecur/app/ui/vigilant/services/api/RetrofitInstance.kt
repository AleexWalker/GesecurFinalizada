package com.gesecur.app.ui.vigilant.services.api

import com.gesecur.app.BuildConfig.API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    val api: SimpleaApi by lazy {
        retrofit.create(SimpleaApi::class.java)
    }
}