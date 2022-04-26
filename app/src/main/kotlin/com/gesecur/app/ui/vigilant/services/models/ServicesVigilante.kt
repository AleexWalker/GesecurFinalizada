package com.gesecur.app.ui.vigilant.services.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServicesVigilante(
        @SerialName("contrato_servicio_id") val contrato_servicio_id: Long,
        @SerialName("vigilante_id") val vigilante_id: Long,
        @SerialName("cuadrante_id") val cuadrante_id: Long,
        @SerialName("fecha_ini") val fecha_ini: String,
        @SerialName("fecha_fin") val fecha_fin: String,
        @SerialName("hora_ini") val hora_ini: String,
        @SerialName("hora_fin") val hora_fin: String,
        @SerialName("descripcion_contrato_servicio") val descripcion_contrato_servicio: String,
        @SerialName("contrato_id") val contrato_id: Long,
        @SerialName("descripcion_contrato") val descripcion_contrato: String,
        @SerialName("nombre_completo") val nombre_completo: String,
        @SerialName("datakey1") val datakey1: Long,
        @SerialName("datakey2") val datakey2: Long,
        @SerialName("telefono") val telefono: String,
        @SerialName("email") val email: String,
        @SerialName("tiene_emergencia") val tiene_emergencia: Boolean
)

/**@Serializable
data class ServicesVigilante(
        @SerialName("contrato_servicio_id") val contrato_servicio_id: Long,
        @SerialName("vigilante_id") val vigilante_id: Long,
        @SerialName("cuadrante_id") val cuadrante_id: Long,
        @SerialName("fecha_ini") val fecha_ini: String,
        @SerialName("fecha_fin") val fecha_fin: String,
        @SerialName("hora_ini") val hora_ini: String,
        @SerialName("hora_fin") val hora_fin: String,
        @SerialName("descripcion_contrato_servicio") val descripcion_contrato_servicio: String,
        @SerialName("contrato_id") val contrato_id: Long,
        @SerialName("descripcion_contrato") val descripcion_contrato: String,
        @SerialName("nombre_completo") val nombre_completo: String,
        @SerialName("datakey1") val datakey1: Long,
        @SerialName("datakey2") val datakey2: Long,
        //@SerialName("turno_id") val turno_id: String,
        //@SerialName("fecha_inicio_turno") val fecha_inicio_turno: String,
        //@SerialName("lat_ini") val lat_ini: String,
        //@SerialName("lon_ini") val lon_ini: String,
        @SerialName("telefono") val telefono: String,
        @SerialName("email") val email: String,
        //@SerialName("fecha_fin_turno") val fecha_fin_turno: String,
        @SerialName("tiene_emergencia") val tiene_emergencia: Boolean
)*/