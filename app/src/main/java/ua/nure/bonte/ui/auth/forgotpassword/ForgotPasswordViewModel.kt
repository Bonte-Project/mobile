package ua.nure.bonte.ui.auth.forgotpassword

import android.R
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
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.DataError
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPassword
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPassword.Event.*
import ua.nure.bonte.ui.auth.signin.SignIn
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val TAG by lazy { ForgotPasswordViewModel::class.simpleName }
    private val _state = MutableStateFlow(ForgotPassword.State())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ForgotPassword.Event>()
    val event = _event.asSharedFlow()

    private var resetPasswordJob: Job? = null
    private var verifyCodeJob: Job? = null
    private var newPasswordJob: Job? = null

    fun onAction(action: ForgotPassword.Action) = viewModelScope.launch {
        when (action) {
            ForgotPassword.Action.OnBack -> {
                _event.emit(OnBack)
            }

            is ForgotPassword.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }

            is ForgotPassword.Action.OnEmailChange -> _state.update { s ->
                s.copy(
                    email = action.email
                )
            }

            ForgotPassword.Action.OnResetPassword -> {
                resetPassword(email = state.value.email)
            }

            ForgotPassword.Action.OnResendCode -> {}

            is ForgotPassword.Action.OnVerifyCode -> {
                verifyCode(code = action.code, email = state.value.email)
            }

            ForgotPassword.Action.OnDismissCodeDialog -> {
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = false
                    )
                }
            }
            ForgotPassword.Action.OnDismissPasswordDialog -> {
                _state.update { s ->
                    s.copy(
                        showNewPasswordDialog = false
                    )
                }
            }
            is ForgotPassword.Action.OnNewPassword -> {
                newPassword(
                    email = state.value.email,
                    password = action.password
                )
            }
        }
    }

    private fun resetPassword(email: String) {
        resetPasswordJob?.cancel()
        resetPasswordJob = viewModelScope.launch {
            authRepository.forgotPassword(
                email = email
            ).onSuccess {
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = true
                    )
                }
            }.onError { error ->
                when(error) {
                    is DataError.ApiError -> {
                        Log.e(TAG, "resetPassword: ${ error.message}", )
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

    private fun verifyCode(code: String, email: String) {
        verifyCodeJob?.cancel()
        verifyCodeJob = viewModelScope.launch {
            authRepository.verifyCode(
                email = email,
                code = code
            ).onSuccess {
                _state.update { s ->
                    s.copy(
                        showVerificationDialog = false,
                        showNewPasswordDialog = true
                    )
                }
            }
        }
    }

    private fun newPassword(password: String, email: String) {
        newPasswordJob?.cancel()
        newPasswordJob = viewModelScope.launch {
            authRepository.resetPassword(
                email = email,
                newPassword = password
            ).onSuccess {
                _state.update { s ->
                    s.copy(
                        showNewPasswordDialog = false
                    )
                }
                _event.emit(ForgotPassword.Event.OnNavigate(route = Screen.Auth.SignIn))
            }
        }
    }
}