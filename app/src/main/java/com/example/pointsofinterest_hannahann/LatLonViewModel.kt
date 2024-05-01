package com.example.pointsofinterest_hannahann

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LatLonViewModel : ViewModel() {
    data class LatLon(var lat: Double, var lon: Double)
    var latLon =  LatLon(51.05,-0.71)
        set(newValue) {
            field = newValue
            liveDataLatLon.value = newValue
        }

    var liveDataLatLon = MutableLiveData<LatLon>()
}