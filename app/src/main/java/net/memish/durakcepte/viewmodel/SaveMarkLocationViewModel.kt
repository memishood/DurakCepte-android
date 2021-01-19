package net.memish.durakcepte.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import net.memish.durakcepte.model.LatLng
import net.memish.durakcepte.model.MarkLocation
import net.memish.durakcepte.service.MarkLocationDatabase

@SuppressLint("MissingPermission")
class SaveMarkLocationViewModel (application: Application): AndroidViewModel(application) {

    private val client = LocationServices.getFusedLocationProviderClient(application.applicationContext)
    val locationMutableLiveData = MutableLiveData<LatLng>()

    private val callback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            p0?.locations?.firstOrNull()?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                client.removeLocationUpdates(this)
                locationMutableLiveData.value = latLng
            }
        }
    }

    fun findPlace() {
        viewModelScope.launch {
            val request = LocationRequest.create()
            request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            client.requestLocationUpdates(
                request,
                callback,
                Looper.myLooper() ?: Looper.getMainLooper()
            )
        }
    }

    fun insert(markLocation: MarkLocation) {
        viewModelScope.launch {
            val dao = MarkLocationDatabase(getApplication()).markLocationDao()
            dao.insertAll(markLocation)
        }
    }

    fun destroy() {
        client.removeLocationUpdates(callback)
    }
}