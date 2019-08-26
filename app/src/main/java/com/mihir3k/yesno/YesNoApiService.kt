package com.mihir3k.yesno

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface YesNoApiService {

    @GET("api")
    suspend fun getAnswer(): Response<Model.Result>

    companion object {
        fun create(): YesNoApiService {
            return Retrofit.Builder()
                .baseUrl("https://yesno.wtf/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YesNoApiService::class.java)
        }
    }

}
