package com.example.demodata.database

import androidx.room.*
import com.example.demodata.model.Movie

@Dao
interface RestoDao {
    @Query("SELECT * FROM `movieData`")
    fun getAll(): List<Movie>

    @Query("SELECT * FROM `movieData` WHERE uid = (:userId)")
    fun loadAllByIds(userId: Int): List<Movie>


    @Insert
    fun insert(vararg order: Movie)

    @Query("DELETE FROM `movieData`")
    fun deleteAll()

    @Query("DELETE FROM `movieData` WHERE id = (:id)")
    fun deleteById(id:Int)

    @Update
    fun updateOrderItem(vararg order: Movie)

}

