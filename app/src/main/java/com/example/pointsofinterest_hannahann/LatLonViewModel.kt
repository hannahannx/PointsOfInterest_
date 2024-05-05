package com.example.pointsofinterest_hannahann

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LatLonViewModel : ViewModel() {
    data class LatLon(val lat: Double = 37.41, val lon: Double = -122.07)

    var latLon = LatLon()
        set(newValue) {
            field = newValue
            liveDataLatLon.value = newValue
        }

    var liveDataLatLon = MutableLiveData<LatLon>()
}