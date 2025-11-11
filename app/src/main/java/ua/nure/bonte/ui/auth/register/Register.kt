package ua.nure.bonte.ui.auth.register

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.auth.signin.SignIn

object Register {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnRegister : Action
        data class OnFirstNameChange(val firstName: String) : Register.Action
        data class OnLastNameChange(val lastName: String) : Register.Action
        data class OnEmailChange(val email: String) : Register.Action
        data class OnPasswordChange(val password: String) : Register.Action
        data class OnConfirmPasswordChange(val confirmPassword: String) : Register.Action

        data class OnPrivacyPolicyAgreementChange(val isAgreed: Boolean) : Register.Action
    }

    data class State(
        val inProgress: Boolean = false,
        val firstName: String = if (BuildConfig.DEBUG) "Natalie" else "",
        val lastName: String = if (BuildConfig.DEBUG) "Smith" else "",
        val email: String = if (BuildConfig.DEBUG) "test@gmail.com" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret1" else "",
        val confirmPassword: String = if (BuildConfig.DEBUG) "Secret1" else "",
        val isPrivacyPolicyAgreed: Boolean = false,
    )
}