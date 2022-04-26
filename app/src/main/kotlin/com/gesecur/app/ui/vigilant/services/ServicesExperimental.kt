package com.gesecur.app.ui.vigilant.services

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.gesecur.app.databinding.ActivityServicesExperimentalBinding
import com.gesecur.app.databinding.CustomAlertDialogBinding
import com.gesecur.app.ui.auth.AuthActivity
import com.gesecur.app.ui.vigilant.VigilantActivity
import com.gesecur.app.ui.vigilant.services.repository.Repository
import com.gesecur.app.utils.getCurrentLocation
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*

class ServicesExperimental(): AppCompatActivity() {

    private lateinit var binding: ActivityServicesExperimentalBinding
    private lateinit var viewModel: ServicesViewModel
    private lateinit var bindingAlert: CustomAlertDialogBinding

    private var vigilantId: Long = 0
    private var vigilantCode: Long = 0

    @SuppressLint("SetTextI18n", "LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityServicesExperimentalBinding.inflate(layoutInflater)
        bindingAlert = CustomAlertDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarTextDay.text = "${getCurrentDay()} ${getCurrentDayNumber()} de ${getCurrentMonth()}"
        loadId()

        /**
         * @param repository: Variable para acceder al método @GET de cada vigilante en sí.
         * @param itemWorkServices: ArrayList cargado con cada uno de los datos obtenidos de la llamada @GET.
         * @param ViewModelFactory: Modelo de vista basado en la recepción de datos.
         * @param viewModel: Administrar los datos recibidos desde la llamada @GET y posteriormente almacenarlos en un MutableList según el 'result' obtenido desde dicha llamada.
         */

        val repository = Repository()
        val itemWorkServices = arrayListOf<ServicesCard>()
        val viewModelFactory = ServicesViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ServicesViewModel::class.java)
        viewModel.getServiciosVigilante(vigilantId, vigilantId)
        viewModel.myResponseGet.observe(this, androidx.lifecycle.Observer { result ->

            Log.e("Prueba", result.status.toString())
            Log.e("Size Array", result.result.size.toString())

            if (result.result.size == 0) {
                binding.textEmptyWorklist.visibility = View.VISIBLE
            } else {
                try {
                    for (i in 0 until result.result.size) {
                        itemWorkServices.add(
                            ServicesCard(
                                result
                                    .result[i]
                                    .contrato_servicio_id.toString(),
                                result
                                    .result[i]
                                    .descripcion_contrato,
                                result
                                    .result[i]
                                    .descripcion_contrato_servicio,
                                "${result.result[i].hora_ini} - ${result.result[i].hora_fin}",
                                "${
                                    result.result[i].fecha_ini.substring(
                                        8,
                                        10
                                    )
                                }/${
                                    result.result[i].fecha_ini.substring(
                                        5,
                                        7
                                    )
                                } - ${
                                    result.result[i].fecha_fin.substring(
                                        8,
                                        10
                                    )
                                }/${result.result[i].fecha_fin.substring(5, 7)}",
                                result.result[i].cuadrante_id.toString(),
                                result.result[i].vigilante_id.toString()
                            )
                        )

                        /**
                         * @param adaptador: Clase serviceAdapater (onClickListener()) para que al hacer click en un item del RecyclerView guarde la información y
                         * posteriormente al iniciar un turno realice una llamada @POST con la información aportada.
                         * @param getCurrentLocation: Función de app.gesecur mediante la cual podemos obtener la ubicación.
                         * @fun saveData: Función para enviar y guardar dichos datos.
                         */

                        val adaptador = ServicesAdapter(itemWorkServices) {

                            getCurrentLocation { location ->
                                if (location != null) {
                                    val postLatitud = location.latitude.toString()
                                    val postLongitud = location.longitude.toString()

                                    saveData(
                                        it.vigilante_id.toLong(),
                                        postLatitud,
                                        postLongitud,
                                        it.cuadrante.toLong()
                                    )
                                }
                            }

                            startActivity(Intent(this, VigilantActivity::class.java))
                        }

                        binding.recyclerServicesVigilant.adapter = adaptador
                        binding.recyclerServicesVigilant.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

                        Log.e("Nº Items", adaptador.itemCount.toString())
                    }
                } catch (e: NullPointerException) {
                    Log.e("Error", e.message.toString())
                }
            }
        })

        binding.exitImage.setOnClickListener {
            generateCustomAlertDialogCloseSession()
        }
    }

    /**
     * Función para generar el díalogo de cerrar sesión y volver a la pantalla de Autentificación
     * @param dialogView: AlertDialog personalizado con Positive y Negative Button
     */

    private fun generateCustomAlertDialogCloseSession() {
        val dialogView = AlertDialog.Builder(this)
        dialogView
            .setMessage("¿Seguro que deseas cerrar sesión?")
            .setPositiveButton("SI", DialogInterface.OnClickListener { dialog, which ->
                finish()
                startActivity(Intent(this, AuthActivity::class.java))
                dialog.dismiss()
            })
            .setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
        val dialog: AlertDialog = dialogView.create()
        dialog.show()

        val pButton: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val nButton: Button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        pButton.setTextColor(Color.BLACK)
        nButton.setTextColor(Color.BLACK)
    }

    /**
     * Función para obtener el código personal de cada vigilante desde AuthFragment.
     * @param vigilantId: Id personal de cada vigilante
     */

    @SuppressLint("LogNotTimber")
    private fun loadId(): Long {
        vigilantId = intent.extras!!.getString("vigilantId", null)!!.toLong()
        vigilantCode = intent.extras!!.getString("vigilantCode", null)!!.toLong()
        if (vigilantId != null) {
            Log.e("Valor de vigilantId", vigilantId.toString())
        } else {
            Log.e("ERROR", "Error al recibir código personal del vigilante")
        }
        return vigilantId
    }

    /**
     * Funciones dedicadas a obtener el Día, el Mes y el Número de dicho día respectivamente.
     * Utilizadas en la toolbar.
     */

    private fun getCurrentDay(): String {
        return SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
            .replaceFirstChar { it.toUpperCase() }
    }

    private fun getCurrentMonth(): String {
        return SimpleDateFormat("MMMM", Locale.getDefault()).format(Date())
            .replaceFirstChar { it.toUpperCase() }
    }

    private fun getCurrentDayNumber(): String {
        return SimpleDateFormat("d", Locale.getDefault()).format(Date())
            .replaceFirstChar { it.toUpperCase() }
    }

    /**
     * Función dedicada a enviar los datos en función del item elegido del RecyclerView por el vigilante.
     * Enviados a VigilantActivity y posteriormente se hará la llamada @POST.
     */

    @SuppressLint("LogNotTimber")
    private fun saveData(vigilanteId: Long, latitud: String, longitud: String, cuadranteId: Long) {
        val sharedPreferences = getSharedPreferences("datosPost", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()

        Log.e("DATOS", "$vigilanteId $latitud $longitud $cuadranteId $vigilantCode")

        editor.putLong("vigilanteId", vigilanteId)
        editor.putString("lat", latitud)
        editor.putString("lon", longitud)
        editor.putLong("cuadranteId", cuadranteId)
        editor.putLong("vigilantCode", vigilantCode)
        editor.apply()
    }
}