package com.gesecur.app.ui.vigilant.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gesecur.app.domain.repositories.user.UserRepositoryImpl
import com.gesecur.app.ui.vigilant.services.repository.Repository

class ServicesViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ServicesViewModel(repository) as T
    }
}