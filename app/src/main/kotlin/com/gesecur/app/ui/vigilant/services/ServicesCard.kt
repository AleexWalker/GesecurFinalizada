package com.gesecur.app.ui.vigilant.services

import java.text.DateFormat

/**
 * Clase dedicada para crear una Card para ServicesAdapter y de esta manera almacenar los datos desde el GSON.
 * @param contrato: (GSON) -> contrato_servicio_id
 * @param servicio: (GSON) -> descripcion_contrato
 * @param localizacion: (GSON) -> contrato_servicio_id
 * @param horas: (GSON) -> contrato_servicio_id
 * @param fechaAbajo: (GSON) -> contrato_servicio_id
 * @param cuadrante: (GSON) -> contrato_servicio_id
 * @param vigilante_id: (GSON) -> contrato_servicio_id
 */

class ServicesCard (val contrato: String,
                    val servicio: String,
                    val localizacion: String,
                    val horas: String,
                    val fechaAbajo: String,
                    val cuadrante: String,
                    val vigilante_id: String)