package net.memish.durakcepte.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import net.memish.durakcepte.model.MarkLocation
import net.memish.durakcepte.service.MarkLocationDatabase

class SavedMarkLocationsViewModel (application: Application): AndroidViewModel(application) {

    val markLocationsMutableLiveData = MutableLiveData<List<MarkLocation>>()

    fun getData() {
        viewModelScope.launch {
            val dao = MarkLocationDatabase(getApplication()).markLocationDao()
            val items = dao.getAll()
            markLocationsMutableLiveData.value = items
        }
    }
}