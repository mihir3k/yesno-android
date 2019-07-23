package com.mihir3k.yesno

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface YesNoApiService {

    @GET("api")
    fun getAnswer(): Observable<Model.Result>

    companion object {
        fun create(): YesNoApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://yesno.wtf/")
                .build()

            return retrofit.create(YesNoApiService::class.java)
        }
    }

}
