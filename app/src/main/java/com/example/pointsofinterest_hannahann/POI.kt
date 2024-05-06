package com.example.pointsofinterest_hannahann

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "pointsofinterest")

data class POI (
    @PrimaryKey (autoGenerate = true) val localId: Long,
    @ColumnInfo(name="osmId")val osmId : Int,
    @ColumnInfo(name="the_name")val name: String,
    @ColumnInfo(name="the_type")val type: String,
    @ColumnInfo(name="the_description")val description: String,
    @ColumnInfo(name="the_lat")val lat: Double,
    @ColumnInfo(name="the_lon")val lon: Double

)