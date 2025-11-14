package ua.nure.bonte.ui.auth.signin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.auth.AuthRepository
import ua.nure.bonte.repository.onError
import ua.nure.bonte.repository.onSuccess
import ua.nure.bonte.ui.auth.signin.SignIn.Event.*
import javax.inject.Inject

@HiltViewModel class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val TAG by lazy { SignInViewModel::class.simpleName }

    private val _event = MutableSharedFlow<SignIn.Event>()
    val event = _event.asSharedFlow()

    private val _state = MutableStateFlow(SignIn.State())
    val state = _state.asStateFlow()

    private var googleSignInJob: Job? = null
    private var signInJob: Job? = null

    fun onAction(action: SignIn.Action) = viewModelScope.launch {
        when(action) {
            SignIn.Action.OnBack -> _event.emit(OnBack)
            is SignIn.Action.OnGoogleSignIn -> onGoogleSignIn(idToken = action.idToken, email = action.email)
            is SignIn.Action.OnNavigate -> _event.emit(OnNavigate(route = action.route))
            SignIn.Action.OnSignIn -> signIn(email = "john.dow@gmail.com", password = "Secret1" )
            is SignIn.Action.OnEmailChange -> _state.update { s ->
                s.copy(
                    email = action.email
                )
            }
            is SignIn.Action.OnPasswordChange -> _state.update { s ->
                s.copy(
                    password = action.password
                )
            }
        }
    }

    private fun onGoogleSignIn(idToken: String, email: String) {
        googleSignInJob?.cancel()
        googleSignInJob = viewModelScope.launch {
            authRepository.googleSignIn(
                token = idToken,
                email = email
            ).onSuccess {

            }.onError { error ->

            }
        }
    }

    private fun signIn(email: String, password: String) {
        signInJob?.cancel()
        signInJob = viewModelScope.launch {
            authRepository.signIn(
                email = email,
                password = password
            ).onSuccess {
                _event.emit(
                    SignIn.Event.OnNavigate(route = Screen.Profile.Dashboard)
                )

            }.onError { error ->
                Log.e(TAG, "signIn: Error: $error" , )

            }
        }
    }
}