package com.gesecur.app.ui.vigilant.services

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gesecur.app.ui.vigilant.services.models.Services
import com.gesecur.app.ui.vigilant.services.repository.Repository
import kotlinx.coroutines.launch

open class ServicesViewModel(private val repository: Repository): ViewModel() {

    val myResponseGet: MutableLiveData<Services> = MutableLiveData()

    fun getServiciosVigilante(userId: Long, vigilantId: Long) {
        viewModelScope.launch {
            val response = repository.getServiciosVigilante(userId, vigilantId)
            myResponseGet.value = response
        }
    }

    /**fun getCurrentUser(): Long {
        return currentUser!!.id
    }*/
}