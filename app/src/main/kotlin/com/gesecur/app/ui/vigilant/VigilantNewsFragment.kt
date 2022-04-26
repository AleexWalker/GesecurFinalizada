package com.gesecur.app.ui.vigilant

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.CustomAlertDialogBinding
import com.gesecur.app.databinding.FragmentVigilantNewsBinding
import com.gesecur.app.domain.models.NewsRegistry
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.dialog.KeepAliveVigilantDialog
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.vigilant.worker.NewsReminderScheduleWorker
import com.gesecur.app.utils.getCurrentLocation
import com.gesecur.app.utils.showAlert
import com.gesecur.app.utils.toToolbarFormat
import com.gesecur.app.utils.toast
import com.google.android.material.card.MaterialCardView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.time.Duration
import java.time.LocalDate
import java.util.*
import java.util.concurrent.TimeUnit

@ToolbarOptions(
    showToolbar = true)
class VigilantNewsFragment : BaseFragment(R.layout.fragment_vigilant_news) {

    private lateinit var alertBinding: CustomAlertDialogBinding

    private val binding by viewBinding(FragmentVigilantNewsBinding::bind)
    private val viewModel by sharedViewModel<VigilantViewModel>()
    private var cuadranteId: Long = 0
    private var vigilantCode: Long = 0

    companion object {
        const val WORK_NAME = "scheduledNewsWork"
    }

    override fun setupViews() {
        setTitle(title = LocalDate.now().toToolbarFormat())

        with(binding) {
            disableAll()

            btnInitTurn.setOnClickListener {
                startTurn()
            }

            btnEndTurn.setOnClickListener {
                generateCustomAlertDialog()
            }

            registryContainer.setOnClickListener { viewModel.goToRegistries() }
            observationsContainer.setOnClickListener { viewModel.goToObservations() }

            btnNoUrgent.setOnClickListener {
                viewModel.addNewRegistry(viewModel.currentTurn.value!!.id, NewsRegistry.TYPE.REQUIREMENT_NON_URGENT)
            }
            btnNoNews.setOnClickListener {
                viewModel.addNewRegistry(viewModel.currentTurn.value!!.id, NewsRegistry.TYPE.NO_NEWS)
            }
            btnEmergency.setOnClickListener {
                viewModel.addNewRegistry(viewModel.currentTurn.value!!.id, NewsRegistry.TYPE.URGENT)
            }
        }
    }

    /**
     * Mediante esta función cargamos un Alert Dialog custom a la hora de finalizar el turno.
     * @param buttonFinalize: Mediante el Material Card, cuando hacemos click en este elemento procedemos a hacer la comprobación
     * de que el usuario haya introducido un código, o si el código introducido coincide con el suyo y posteriormente si dichos
     * códigos coinciden el vigilante finalizaría el turno.
     * @param buttonCancel: Si por equivocación ha hecho click en finalizar turno de esta manera volverá a la pestaña principal.
     * @param ediTextVigilantCode: Recogemos el código introducido por el vigilante.
     */

    private fun generateCustomAlertDialog() {

        getVigilantData()

        val view = View.inflate(context, R.layout.custom_alert_dialog, null)
        val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
        builder!!.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

        val buttonFinalize = view.findViewById<MaterialCardView>(R.id.custom_card_finalize)
        val buttonCancel = view.findViewById<MaterialCardView>(R.id.custom_card_cancel)
        val editTextVigilantCode = view.findViewById<EditText>(R.id.alertDialogEditText)

        buttonFinalize.setOnClickListener {
            if (editTextVigilantCode.text.isEmpty()) {
                Toast.makeText(context, "¡Introduce tu código de vigilante!", Toast.LENGTH_SHORT).show()
            } else {
                Log.e("vigilantCode", vigilantCode.toString())
                Log.e("editTextVigilantCode", editTextVigilantCode.text.toString())
                if (vigilantCode.toString() == editTextVigilantCode.text.toString()) {
                    endTurn()
                    dialog.dismiss()
                } else {
                    Toast.makeText(context, "¡Código Incorrecto!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        buttonCancel.setOnClickListener {
            Log.e("Prueba", "Cancel")
            dialog.dismiss()
        }
    }

    override fun stateManagedViewModels() = arrayListOf(viewModel)

    override fun setupViewModels() {
        viewModel.viewAction.observe(viewLifecycleOwner, {
            when(it) {
                is VigilantViewModel.Action.TurnStarted -> onTurnStarted()
                is VigilantViewModel.Action.TurnFinished -> onTurnFinished()
                is VigilantViewModel.Action.TurnStartedSeconds -> printTurnStartedSeconds(it.seconds)
                is VigilantViewModel.Action.RegistrySucess -> toast(R.string.VIGILANT_NEWS_REGISTRY_SUCCESS)
            }
        })

        viewModel.currentTurn.observe(viewLifecycleOwner) {

        }

        viewModel.getCurrentTurn()
    }

    private fun disableAll() {
        with(binding) {
            btnEmergency.isEnabled = false
            btnNoNews.isEnabled = false
            btnNoUrgent.isEnabled = false
            observationsContainer.alpha = .3f
            registryContainer.alpha = .3f
            observationsContainer.isClickable = false
            registryContainer.isClickable = false
        }
    }

    private fun enableAll() {
        with(binding) {
            btnEmergency.isEnabled = true
            btnNoNews.isEnabled = true
            btnNoUrgent.isEnabled = true
            observationsContainer.alpha = 1f
            registryContainer.alpha = 1f
            observationsContainer.isClickable = true
            registryContainer.isClickable = true
        }
    }

    private fun startTurn() {
        showLoadingDialog()
        getVigilantData()

        requireActivity().getCurrentLocation {
            it?.let { viewModel.startTurn(it.latitude, it.longitude, cuadranteId) }

            manageKeepAliveNotification()
        }
    }

    /**
     * Función para recoger los datos envíados de la Activity anterior.
     * @param cuadranteId: Obtenemos los datos del cuadrante del trabajo envíados por la Activity anterior mediante un SharedPreferences.
     * @param vigilantCode: Obtenemos el código introducido al comienzo por el vigilante y posteriormente lo comparamos al finalizar el turno.
     */

    private fun getVigilantData() {
        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences("datosPost", Context.MODE_PRIVATE)
        cuadranteId = sharedPreferences.getLong("cuadranteId", 0)
        vigilantCode = sharedPreferences.getLong("vigilantCode", 0)
        Log.e("CuadId", cuadranteId.toString())
        Log.e("VigiId", vigilantCode.toString())
    }

    private fun endTurn() {
        showLoadingDialog()

        requireActivity().getCurrentLocation {
            it?.let { viewModel.endTurn(viewModel.currentTurn.value!!.id, it.latitude, it.longitude) }
        }
    }

    private fun onTurnStarted() {
        binding.btnInitTurn.isVisible = false
        binding.activeTurnContainer.isVisible = true

        enableAll()
    }

    private fun onTurnFinished() {
        showAlert(text = R.string.VIGILANT_TURN_FINISHED_SUCCESS, listener = { _, _ ->
            disableAll()
            cancelPeriodicalNotification()
            viewModel.closeSession()

            navigateToSplash()
        })
    }

    private fun printTurnStartedSeconds(duration: Duration) {
        binding.tvActiveTurnTimer.text = String.format(
            Locale.getDefault(),
            "%02d:%02d:%02d",
            duration.toHours(),
            duration.toMinutes() % 60,
            duration.seconds % 60)
    }

    private fun manageKeepAliveNotification() {
        KeepAliveVigilantDialog(requireContext()) { generatePeriodicalNotification(it) }.show()
    }

    private fun generatePeriodicalNotification(time: Int) {
        val periodicWork =
            PeriodicWorkRequestBuilder<NewsReminderScheduleWorker>(time.toLong(), TimeUnit.MINUTES)
                .setInitialDelay(time.toLong(), TimeUnit.MINUTES)
                .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.REPLACE,
                periodicWork
            )
    }

    private fun cancelPeriodicalNotification() {
        WorkManager.getInstance(requireContext())
            .cancelAllWork()
    }
}