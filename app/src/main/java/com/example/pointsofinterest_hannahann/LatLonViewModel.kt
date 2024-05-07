package com.example.pointsofinterest_hannahann

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LatLonViewModel : ViewModel() {
    data class LatLon(val lat: Double = 37.41, val lon: Double = -122.07)
    data class GpsStatus(var status: Boolean = true)

    var latLon = LatLon()
        set(newValue) {
            field = newValue
            liveDataLatLon.value = newValue
        }

    var gpsStatus = GpsStatus()
        set(newStatus){
            field = newStatus
            liveGpsStatus.value = newStatus
        }

    var liveGpsStatus = MutableLiveData<GpsStatus>()
    var liveDataLatLon = MutableLiveData<LatLon>()
}