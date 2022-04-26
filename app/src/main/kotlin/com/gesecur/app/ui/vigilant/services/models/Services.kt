package com.gesecur.app.ui.vigilant.services.models

import kotlinx.serialization.SerialName

data class Services(
    @SerialName("status") val status: Long,
    @SerialName("result") val result: ArrayList<ServicesVigilante>,
)