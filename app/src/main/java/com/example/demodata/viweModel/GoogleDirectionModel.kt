package com.example.demodata.viweModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.demodata.R
import com.example.demodata.apiu.RestClient
import com.example.demodata.model.GoogleDirection
import com.google.android.gms.maps.model.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleDirectionModel : ViewModel() {


    fun getRoute(mContext: Context, origin: LatLng, destination: LatLng): LiveData<GoogleDirection> {

        var data = MutableLiveData<GoogleDirection>()

        var url =
            "directions/json?origin=" + origin.latitude + "," + origin.longitude + "&destination=" + destination.latitude + "," + destination.longitude + "&key=" + mContext.getString(
                R.string.google_maps_key
            )


        Log.e("googledrawUrl", "" + url)
        val call = RestClient.getGoogle()?.GetDirectionRoute(url)

        call?.enqueue(object : Callback<GoogleDirection> {
            override fun onFailure(call: Call<GoogleDirection>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<GoogleDirection>, response: Response<GoogleDirection>) {
                if (response.body()?.routes?.isNotEmpty()!!)
                    data.value = response.body()
                else
                    data.value = null
            }

        })


        return data
    }


}
