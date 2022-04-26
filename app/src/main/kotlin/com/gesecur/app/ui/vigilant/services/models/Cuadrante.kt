package com.gesecur.app.ui.vigilant.services.model

import kotlinx.serialization.SerialName

data class Cuadrante(
    @SerialName("vigilante_id") val vigilante_id: Long,
    @SerialName("lat") val lat: Long,
    @SerialName("lon") val lon: Long,
    @SerialName("cuadrante_id") val cuadrante_id: Long,
)