package com.gesecur.app.ui.vigilant.services.api

import com.gesecur.app.ui.vigilant.services.models.Services
import retrofit2.Response
import retrofit2.http.*

interface SimpleaApi {

    @GET("/vigilantes/cuadrantes/contrato/servicio/vigilante/{user}/{vigilante}")
    suspend fun getServiciosVigilante(
        @Path("user") userId: Long,
        @Path("vigilante") vigilanteId: Long
    ): Services

    @POST("app/turno/start")
    @FormUrlEncoded
    suspend fun postServicio(
        @Field("vigilante_id") vigilante_id: Long,
        @Field("lat") lat: Long,
        @Field("lon") lon: Long,
        @Field("cuadrante_id") cuadrante_id: Long
    ): Response<Long>
}

/**
 @Path("user") userId: Long,
 @Path("vigilante") vigilanteId: Long
 */