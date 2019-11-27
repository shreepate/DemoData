package com.example.demodata.viweModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.demodata.MyApplication
import com.example.demodata.apiu.RestCallback
import com.example.demodata.apiu.RestClient
import com.example.demodata.model.MovieList

import com.squareup.okhttp.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MovieListApi() {


    fun getMovieList(): LiveData<MovieList> {
        val data = MutableLiveData<MovieList>()




        var call = RestClient.get()!!.moverListOmDb("movie","671de40e","inception")
        call.enqueue(object : RestCallback<MovieList>(MyApplication.instance) {
            override fun Success(response: retrofit2.Response<MovieList>) {



                data.value = response.body()

            }

            override fun failure() {
                data.value = null
            }

        })






        return data
    }

}