package net.memish.durakcepte.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import net.memish.durakcepte.model.MarkLocation
import net.memish.durakcepte.service.MarkLocationDatabase

class SavedMarkLocationDetailsViewModel(application: Application): AndroidViewModel(application) {
    val deletedMarkLocationMutableLiveData = MutableLiveData<MarkLocation>()

    fun delete(markLocation: MarkLocation) {
        viewModelScope.launch {
            val dao = MarkLocationDatabase(getApplication()).markLocationDao()
            dao.delete(markLocation)
            deletedMarkLocationMutableLiveData.value = markLocation
        }
    }
}