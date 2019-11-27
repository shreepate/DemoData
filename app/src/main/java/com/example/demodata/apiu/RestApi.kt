package com.example.demodata.apiu

import com.example.demodata.model.GoogleDirection
import com.example.demodata.model.MovieList
import retrofit2.Call
import retrofit2.http.*
import retrofit2.http.GET




public interface RestApi {


    @GET("/")
    fun moverListOmDb(@Query("t") type:String, @Query("apikey") apiKey:String, @Query("s") search:String): Call<MovieList>
    @GET
    fun GetDirectionRoute(@Url url: String): Call<GoogleDirection>
}