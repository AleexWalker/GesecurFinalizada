package com.gesecur.app.ui.profile

import androidx.lifecycle.LiveData
import arrow.core.Either
import arrow.core.getOrElse
import com.gesecur.app.domain.models.*
import com.gesecur.app.domain.repositories.incidence.IncidenceRepository
import com.gesecur.app.domain.repositories.personal.PersonalRepository
import com.gesecur.app.domain.repositories.user.UserRepository
import com.gesecur.app.ui.common.arch.BaseAction
import com.gesecur.app.ui.common.arch.SingleLiveEvent
import com.gesecur.app.ui.common.arch.State
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.incidences.IncidencesViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.time.LocalDate

class PersonalViewModel(
    private val userRepository: UserRepository,
    private val personalRepository: PersonalRepository,
    private val incidenceRepository: IncidenceRepository
) : BaseViewModel() {

    protected var currentUser: User? = userRepository.getUser().getOrElse { null }

    //
    sealed class MainAction: BaseAction() {
        object navigateToAddIncidence: MainAction()
    }
    //

    sealed class Action: BaseAction() {
        object ExpenseAdded : Action()
        object MileageAdded : Action()
        object ExpenseDeleted : Action()
        object MileageDeleted : Action()
        //
        object onIncidenceAdded: Action()
        class onLocationChanged(val lat: Double, val lon: Double): Action()
        //
    }

    //
    init {
        getUserIncidenceTypes()
    }
    //

    protected val _expenses: SingleLiveEvent<List<Expense>> = SingleLiveEvent()
    val expenses: LiveData<List<Expense>>
        get() = _expenses

    protected val _expenseTypes: SingleLiveEvent<List<ExpenseType>> = SingleLiveEvent()
    val expenseTypes: LiveData<List<ExpenseType>>
        get() = _expenseTypes

    protected val _vehicles: SingleLiveEvent<List<Vehicle>> = SingleLiveEvent()
    val vehicles: LiveData<List<Vehicle>>
        get() = _vehicles

    protected val _mileage: SingleLiveEvent<List<Mileage>> = SingleLiveEvent()
    val mileage: LiveData<List<Mileage>>
        get() = _mileage

    //
    protected val _mainViewAction: SingleLiveEvent<IncidencesViewModel.MainAction> = SingleLiveEvent()
    val mainViewAction: LiveData<IncidencesViewModel.MainAction>
        get() = _mainViewAction

    protected val _incidences: SingleLiveEvent<List<Incidence>> = SingleLiveEvent()
    val incidences: LiveData<List<Incidence>>
        get() = _incidences

    protected val _incidenceTypes: SingleLiveEvent<List<IncidenceType>> = SingleLiveEvent()
    val incidenceTypes: LiveData<List<IncidenceType>>
        get() = _incidenceTypes
    //

     fun getCurrentExpenses() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserExpenses(currentUser!!.id, currentUser!!.personalId)) {
            is Either.Right -> {
                _expenses.value = result.b
                _viewState.value = if(result.b.isEmpty()) State.Empty else State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getExpenseTypes() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserExpenseTypes(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _expenseTypes.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addExpense(expense: Expense, file: File?) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.addUserExpense(currentUser!!.id, currentUser!!.personalId, expense, file)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.ExpenseAdded
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun deleteExpense(expense: Expense) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.deleteExpense(currentUser!!.id, expense.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.ExpenseDeleted
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getCurrentMileage() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserMileage(currentUser!!.id, currentUser!!.personalId)) {
            is Either.Right -> {
                _mileage.value = result.b
                _viewState.value = if(result.b.isEmpty()) State.Empty else State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getVehicles() = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.getUserVehicles(currentUser!!.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _vehicles.value = result.b
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addMileage(mileage: Mileage, vehicle: Vehicle, file: File?) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.addUserMileage(currentUser!!.id, currentUser!!.personalId, mileage, vehicle, file)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.MileageAdded
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun deleteMileage(mileage: Mileage) = launch {
        _viewState.value = State.Loading

        when (val result = personalRepository.deleteMileage(currentUser!!.id, mileage.id)) {
            is Either.Right -> {
                _viewState.value = State.Success
                _viewAction.value = Action.MileageDeleted
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun closeSession() = launch {
        userRepository.clearAccessToken()
    }

    //
    fun getUserIncidences(date: LocalDate) = launch {
        _viewState.value = State.Loading

        when (val result = incidenceRepository.getIncidences(currentUser!!.personalId)) {
            is Either.Right -> {
                with(result.b/*.filter { it.dateCreation?.toLocalDate() == date }*/) {
                    _incidences.value = this
                    _viewState.value = if(this.isEmpty()) com.gesecur.app.ui.common.arch.State.Empty else com.gesecur.app.ui.common.arch.State.Success
                }
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun getUserIncidenceTypes() = launch {
        _viewState.value = State.Loading

        when (val result = incidenceRepository.getIncidenceTypes(currentUser!!.id)) {
            is Either.Right -> {
                _incidenceTypes.value = result.b
                _viewState.value = State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun addIncidence(incidence: Incidence, file: File?) = launch {
        _viewState.value = State.Loading

        when (val result = incidenceRepository.addIncidence(currentUser!!.personalId, currentUser!!.id, incidence, file)) {
            is Either.Right -> {
                _viewAction.value = IncidencesViewModel.Action.onIncidenceAdded
                _viewState.value = State.Success
            }

            is Either.Left -> {
                _viewState.value = State.Success
                _viewAction.value = BaseAction.ShowError(result.a)
            }
        }
    }

    fun changeIncidenceLocation(lat: Double, lon: Double) {
        _viewAction.value = IncidencesViewModel.Action.onLocationChanged(lat, lon)
    }

    fun goToAddIncidence() {
        _mainViewAction.value = IncidencesViewModel.MainAction.navigateToAddIncidence
    }
}