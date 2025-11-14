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
    private val PASSWORD_MISMATCH_ERROR = "Passwords do not match"

    private val _state = MutableStateFlow(Register.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<Register.Event>()
    val event = _event.asSharedFlow()

    private var registrationJob: Job? = null

    fun onAction(action: Register.Action) = viewModelScope.launch {
        when (action) {
            Register.Action.OnBack -> _event.emit(Register.Event.OnBack)
            is Register.Action.OnNavigate -> _event.emit(OnNavigate(route = action.route))

            Register.Action.OnRegister -> {
                val currentState = state.value

                if (currentState.password != currentState.confirmPassword) {
                    _state.update { it.copy(passwordMismatchError = PASSWORD_MISMATCH_ERROR) }
                    return@launch
                }
                if (!currentState.isPrivacyPolicyAgreed) return@launch

                _state.update { it.copy(passwordMismatchError = null, inProgress = true) }

                authRepository.forgotPassword(email = currentState.email)
                    .onSuccess {
                        _state.update { it.copy(showVerificationSheet = true, inProgress = false) }
                    }
                    .onError { error ->
                        Log.e(TAG, "Failed to send verification code: $error")
                        _state.update { it.copy(verificationError = "Failed to send code. Try again.", inProgress = false) }
                    }
            }

            is Register.Action.OnVerificationCodeChange -> _state.update { s ->
                s.copy(verificationCode = action.code, verificationError = null)
            }

            Register.Action.OnResendCodeClick -> {
                val currentState = state.value
                Log.d(TAG, "Resending code for ${currentState.email}")
                // Надсилаємо код повторно
                authRepository.forgotPassword(email = currentState.email)
                    .onError { error ->
                        Log.e(TAG, "Failed to resend verification code: $error")
                        _state.update { it.copy(verificationError = "Failed to resend code.") }
                    }
            }

            Register.Action.OnVerificationConfirmed -> {
                val currentState = state.value
                _state.update { it.copy(inProgress = true, verificationError = null) }

                authRepository.verifyCode(email = currentState.email, code = currentState.verificationCode)
                    .onSuccess {
                        _state.update {
                            it.copy(
                                showVerificationSheet = false,
                                showSuccessSheet = true,
                                inProgress = false
                            )
                        }
                    }
                    .onError {
                        _state.update {
                            it.copy(
                                verificationError = "Invalid code. Please try again or resend.",
                                inProgress = false
                            )
                        }
                    }
            }

            Register.Action.OnVerificationSuccessAcknowledge -> {
                _state.update { it.copy(showSuccessSheet = false, inProgress = true) }


                register(
                    fullName = "${state.value.firstName} ${state.value.lastName}",
                    email = state.value.email,
                    password = state.value.password,
                    role = "user"
                )
            }
            is Register.Action.OnFirstNameChange -> _state.update { s -> s.copy(firstName = action.firstName) }
            is Register.Action.OnLastNameChange -> _state.update { s -> s.copy(lastName = action.lastName) }
            is Register.Action.OnEmailChange -> _state.update { s -> s.copy(email = action.email) }
            is Register.Action.OnPasswordChange -> _state.update { s -> s.copy(password = action.password, passwordMismatchError = null) }
            is Register.Action.OnConfirmPasswordChange -> _state.update { s -> s.copy(confirmPassword = action.confirmPassword, passwordMismatchError = null) }
            is Register.Action.OnPrivacyPolicyAgreementChange -> _state.update { s -> s.copy(isPrivacyPolicyAgreed = action.isAgreed) }
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
                fullName = fullName, email = email, password = password, role = role
            ).onSuccess { message ->
                Log.d(TAG, "Registration success: $message")
                _event.emit(Register.Event.OnRegistrationSuccess)
            }.onError { error: DataError ->
                Log.e(TAG, "Registration error: $error")
            }
        }
    }
}