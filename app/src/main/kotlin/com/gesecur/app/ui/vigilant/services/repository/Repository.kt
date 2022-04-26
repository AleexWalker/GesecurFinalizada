package com.gesecur.app.ui.vigilant.services.repository

import com.gesecur.app.ui.vigilant.services.api.RetrofitInstance
import com.gesecur.app.ui.vigilant.services.models.Services

class Repository() {
    suspend fun getServiciosVigilante(userId: Long, vigilantId: Long): Services {
        return RetrofitInstance.api.getServiciosVigilante(userId, vigilantId)
    }
}
