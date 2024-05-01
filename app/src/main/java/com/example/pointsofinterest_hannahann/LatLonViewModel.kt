package com.example.pointsofinterest_hannahann

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class LatLon(var lat: Double=51.05, var lon: Double=-0.72)
class LatLonViewModel : ViewModel() {
    var latLon =  LatLon()
        set(newVal) {
            field = newVal
            liveLatLon.value = newVal
        }

    var liveLatLon = MutableLiveData<LatLon>()
}