package ua.nure.bonte.ui.auth.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.navigation.Screen
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
    private var resendCodeJob: Job? = null
    private var verifyCodeJob: Job? = null

    fun onAction(action: Register.Action) = viewModelScope.launch {
        when (action) {
            Register.Action.OnBack -> _event.emit(OnBack)
            is Register.Action.OnNavigate -> _event.emit(OnNavigate(route = action.route))

            Register.Action.OnRegister -> {
                register(
                    fullName = "${state.value.firstName} ${state.value.lastName}",
                    email = state.value.email,
                    password = state.value.password,
                    role = "user"
                )
            }

            Register.Action.OnResendCodeClick -> {
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = false
                    )
                }
                resendCode(email = state.value.email)
            }

            is Register.Action.OnVerificationEmailCode -> {
                verifyEmail(code = action.code, email = state.value.email)
            }

            is Register.Action.OnFirstNameChange -> _state.update { s -> s.copy(firstName = action.firstName) }
            is Register.Action.OnLastNameChange -> _state.update { s -> s.copy(lastName = action.lastName) }
            is Register.Action.OnEmailChange -> _state.update { s -> s.copy(email = action.email) }
            is Register.Action.OnPasswordChange -> _state.update { s -> s.copy(password = action.password) }
            is Register.Action.OnConfirmPasswordChange -> _state.update { s -> s.copy(confirmPassword = action.confirmPassword) }
            is Register.Action.OnPrivacyPolicyAgreementChange -> _state.update { s -> s.copy(isPrivacyPolicyAgreed = action.isAgreed) }
            Register.Action.OnDismissEmailVerification -> {
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = false
                    )
                }
            }
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
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = true
                    )
                }
            }.onError { error: DataError ->
                Log.e(TAG, "Registration error: $error")
            }
        }
    }

    private fun resendCode(email: String) {
        resendCodeJob?.cancel()
        resendCodeJob = viewModelScope.launch {
            authRepository.forgotPassword(
                email = email
            ).onSuccess {
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = true
                    )
                }
            }.onError {

            }

        }
    }

    private fun verifyEmail(code: String, email: String) {
        verifyCodeJob?.cancel()
        verifyCodeJob = viewModelScope.launch {
            authRepository.verifyEmail(
                email = email,
                code = code
            ).onSuccess {
                _event.emit(Register.Event.OnNavigate(route = Screen.Auth.SignIn))

            }.onError {

            }
        }

    }
}