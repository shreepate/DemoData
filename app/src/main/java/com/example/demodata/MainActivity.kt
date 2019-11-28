package com.example.demodata

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demodata.adapter.adapter
import com.example.demodata.database.AppDatabase
import com.example.demodata.model.Movie
import com.example.demodata.viweModel.MovieListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.error_view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var adapter: adapter? = null
    var movieList: ArrayList<Movie?> = ArrayList()
    private val REQUEST_CODE_Location_PERMISSIONS = 6
    var db: AppDatabase? = null
    var localList: ArrayList<Movie> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        db = AppDatabase.getDatabase(this!!)
        initialization()
        getDataFromLocal()

//        btnRetry.setOnClickListener {
//            getMovie()
//        }
    }

    private fun getDataFromLocal() {
        doAsync {
            localList = db?.orderLog()?.getAll()!! as ArrayList<Movie>
            uiThread {
                if (localList.isNullOrEmpty())
                    getMovie()
                else {
                    movieList.clear()
                    movieList.addAll(localList)
                    adapter?.notifyDataSetChanged()
                    Log.e("data", localList[0].title)
                    Log.e("list size is", localList.size.toString())
                }
//

            }
        }
    }

    private fun initialization() {

        recycleView.setLayoutManager(LinearLayoutManager(this))
        recycleView.setHasFixedSize(true)
        adapter = adapter(this@MainActivity, object : adapter.OnItemClick {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onClicklisneter(pos: Int, name: String) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    permissionLocation()
                else {
                    val intent = Intent(this@MainActivity, MapActivity::class.java)
                    intent.putExtra("lat", "23.0272300")
                    intent.putExtra("lng", "72.5616200")
                    intent.putExtra("name", movieList[pos]!!.title)

                    startActivity(intent)
                }
            }

        }, movieList)
        recycleView.setAdapter(adapter)

        // View Model

    }

    /**
     * get Movie List from api
     *
     * @param @null
     */
    private fun getMovie() {
       var  moviewListViewModel = ViewModelProviders.of(this).get(MovieListViewModel::class.java)
//        recycleView.stateViewState = MyRecycleView.RecyclerViewStateEnum.LOADING
        moviewListViewModel?.getMovieListResponseLiveData()?.observe(this, Observer {
            if (it != null) {
                movieList.addAll(it.search)
                if (localList.isNullOrEmpty())
                    insertIntoLocalDb()
                adapter?.notifyDataSetChanged()
            } else {
//                recycleView.stateViewState = MyRecycleView.RecyclerViewStateEnum.ERROR
            }

        })
    }

    fun insertIntoLocalDb() {
        doAsync {
            for (i in 0 until movieList.size)
                db?.orderLog()?.insert(movieList[i]!!)


            uiThread {
                getDataFromLocal()
            }
        }
    }


    private fun addPermission(permission: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(permission) !== android.content.pm.PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun permissionLocation() {
        if (!addPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_Location_PERMISSIONS
            )


        } else {
            val intent = Intent(this@MainActivity, MapActivity::class.java)
            intent.putExtra("lat", "23.0272300")
            intent.putExtra("lng", "72.5616200")
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_Location_PERMISSIONS -> {
                if (grantResults.size > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        val intent = Intent(this@MainActivity, MapActivity::class.java)
                        intent.putExtra("lat", "23.0272300")
                        intent.putExtra("lng", "72.5616200")
                        startActivity(intent)

                    } else {
                        Log.e("permission", "read location permission denied")

                    }
                }
            }

            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


}
