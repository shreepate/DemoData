package com.example.demodata.viweModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.demodata.model.MovieList

class MovieListViewModel() : ViewModel() {
    fun getMovieListResponseLiveData(): LiveData<MovieList> {

        return MovieListApi().getMovieList()
    }
}