package ua.nure.bonte.ui.auth.forgotpassword

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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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


    fun onAction(action: ForgotPassword.Action) = viewModelScope.launch {
        when (action) {
            ForgotPassword.Action.OnBack -> {
                _event.emit(ForgotPassword.Event.OnBack)
            }

            is ForgotPassword.Action.OnNavigate -> {
                _event.emit(OnNavigate(route = action.route))
            }
            is ForgotPassword.Action.OnEmailChange -> _state.update { s ->
                s.copy(
                    email = action.email
                )
            }
        }
    }
}