package com.example.pointsofinterest_hannahann

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao

@Dao
interface PoiDAO {
    @Query("SELECT * FROM pointsofinterest WHERE localId=:id")
    fun getPOIById(id: Long): POI?

    @Query("SELECT * FROM pointsofinterest")
    fun getAllPOI(): LiveData<List<POI>>

    @Insert
    fun add(student: POI) : Long

    @Update
    fun update(student: POI) : Int

    @Delete
    fun delete(student: POI) : Int
}
class DAO {
}