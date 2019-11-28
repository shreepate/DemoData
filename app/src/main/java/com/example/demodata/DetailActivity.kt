package com.example.demodata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.demodata.database.AppDatabase
import com.example.demodata.model.Movie
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_tutorial_layout.*
import kotlinx.android.synthetic.main.item_tutorial_layout.view.*

class DetailActivity : AppCompatActivity() {
    var movieList: ArrayList<Movie?> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (intent != null && intent.hasExtra("movie"))
            movieList = intent.getSerializableExtra("movie") as ArrayList<Movie?>

        val adapter = UltraPagerAdapter(true, movieList)
        view_pager2.setAdapter(adapter)
        indicator.setupWithViewPager(view_pager2)

    }

    inner class UltraPagerAdapter(private val isMultiScr: Boolean, val list: ArrayList<Movie?>) : PagerAdapter() {

        override fun getCount(): Int {
            return list.size

        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val linearLayout =
                LayoutInflater.from(container.context).inflate(
                    R.layout.item_tutorial_layout,
                    null
                ) as LinearLayout
            linearLayout.img_slider.setImageURI(list[position]!!.poster)
            container.addView(linearLayout)
            return linearLayout

        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as LinearLayout
            container.removeView(view)
        }
    }

}