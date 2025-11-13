package ua.nure.bonte.ui.auth.forgotpassword

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPassword
import ua.nure.bonte.ui.auth.register.Register

object ForgotPassword {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action{
        data object OnBack : ForgotPassword.Action
        data class OnNavigate(val route: Screen) : ForgotPassword.Action
        data class OnEmailChange(val email: String) : ForgotPassword.Action
    }

    data class State(
        val email: String = if (BuildConfig.DEBUG) "test@gmail.com" else "",
    )
}