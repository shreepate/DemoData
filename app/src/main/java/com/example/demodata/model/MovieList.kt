package com.example.demodata.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class MovieList(
    @SerializedName("Response")
    val response: String = "",
    @SerializedName("Search")
    val search: List<Movie> = listOf(),
    @SerializedName("totalResults")
    val totalResults: String = ""
) : Serializable

@Entity(tableName = "MovieData")
data class Movie(
    @SerializedName("imdbID")
    val imdbID: String = "",
    @SerializedName("Poster")
    val poster: String = "",
    @SerializedName("Title")
    val title: String = "",
    @SerializedName("Type")
    val type: String = "",
    @SerializedName("Year")
    val year: String = ""
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
    var uid: Int = 0
}