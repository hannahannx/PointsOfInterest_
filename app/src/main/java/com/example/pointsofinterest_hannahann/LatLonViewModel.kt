package com.example.pointsofinterest_hannahann

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LatLonViewModel : ViewModel() {
    data class LatLon(val lat: Double, val lon: Double)
    var latLon =  LatLon(37.41,-122.07)
        set(newValue) {
            field = newValue
            liveDataLatLon.value = newValue
        }

    var liveDataLatLon = MutableLiveData<LatLon>()
}