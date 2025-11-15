package ua.nure.bonte.ui.auth.register

import ua.nure.bonte.BuildConfig
import ua.nure.bonte.navigation.Screen

object Register {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnNavigate(val route: Screen) : Action
        data object OnRegister : Action

        data class OnVerificationEmailCode(val code: String) : Action
        data object OnResendCodeClick : Action
        data class OnFirstNameChange(val firstName: String) : Action
        data class OnLastNameChange(val lastName: String) : Action
        data class OnEmailChange(val email: String) : Action
        data class OnPasswordChange(val password: String) : Action
        data class OnConfirmPasswordChange(val confirmPassword: String) : Action

        data class OnPrivacyPolicyAgreementChange(val isAgreed: Boolean) : Action
        data object OnDismissEmailVerification : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val firstName: String = "",
        val lastName: String = "",
        val email: String = if (BuildConfig.DEBUG) "john.dow@gmail.com" else "",
        val password: String = if (BuildConfig.DEBUG) "Secret1" else "",
        val confirmPassword: String = if (BuildConfig.DEBUG) "Secret1" else "",
        val isPrivacyPolicyAgreed: Boolean = false,
        val showVerificationDialog: Boolean = false,
        val code: String = "",
    )
}