package com.example.demodata.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.demodata.R
import com.example.demodata.model.Movie
import kotlinx.android.synthetic.main.item.view.*


class adapter(
    val context: Activity,
    val onItemClick: OnItemClick,
    val movieList: ArrayList<Movie?>

) :
    RecyclerView.Adapter<adapter.MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)

        return MovieViewHolder(v, context)

    }


    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder.bind(movieList[position],position,onItemClick)


    }

    override fun getItemCount(): Int {
        // return timeSlot.size
        return movieList.size
    }

    class MovieViewHolder(itemView: View, context: Activity) : RecyclerView.ViewHolder(itemView) {


        fun bind(
            movie: Movie?,
            position: Int,
            onItemClick: OnItemClick
        ) =
            with(itemView) {
                tvTitle.text=movie?.title.toString()
                sdPoster.setImageURI(movie?.poster)
                sdPoster2.setImageURI("http://192.249.121.94/~mobile/interview/public/images/csm_shutterstock_73748515_01_cf1fd34057_519ffe33ac.jpg\"")
                ll_main.tag=position
                ll_main.setOnClickListener {
                    onItemClick.onClicklisneter(it.tag as Int,"item")
                }
            }


    }

    interface OnItemClick {
        fun onClicklisneter(pos: Int, name: String)

    }


}