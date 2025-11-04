package ua.nure.bonte.ui.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.ui.auth.register.Register.Event.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val TAG by lazy { RegisterViewModel::class.simpleName }
    private val _state = MutableStateFlow(Register.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Register.Event>()
    val event = _event.asSharedFlow()

    private var registrationJob: Job? = null

    fun onAction(action: Register.Action) = viewModelScope.launch {
        when (action) {
            Register.Action.OnBack -> {
                _event.emit(Register.Event.OnBack)
            }

            is Register.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

            Register.Action.OnRegister -> register(
                fullName = "John Dow",
                email = "john.dow@gmail.com",
                password = "Secret1",
                role = "user"
            )
        }
    }

    private fun register(
        fullName: String,
        email: String,
        password: String,
        role: String,
    ) {
        registrationJob?.cancel()
        registrationJob = viewModelScope.launch {
            authRepository.register(
                fullName = fullName,
                email = email,
                password = password,
                role = role
            ).onSuccess { message ->
                Log.d(TAG, "register: $message")
            }.onError { error: DataError ->
                when(error) {
                    is DataError.ApiError -> {
                        Log.e(TAG, "register: ${error.message}", )
                    }
                    DataError.Remote.REQUEST_TIMEOUT,
                    DataError.Remote.TOO_MANY_REQUESTS,
                    DataError.Remote.NO_INTERNET,
                    DataError.Remote.SERVER,
                    DataError.Remote.SERIALIZATION,
                    DataError.Remote.UNKNOWN -> Unit
                }
            }
        }

    }
}