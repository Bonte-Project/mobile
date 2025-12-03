package ua.nure.bonte.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable data object EmptyScreen : Screen()

    @Serializable sealed class Auth: Screen() {
        @Serializable data object Registration : Auth()
        @Serializable data object SignIn : Auth()
        @Serializable data object ForgotPassword : Auth()
    }
    @Serializable sealed class Profile: Screen() {
        @Serializable data object Dashboard : Profile()
        @Serializable data object Settings : Profile()
        @Serializable data object AddMenu : Profile()
        @Serializable data object Nutrition : Profile()
    }

    @Serializable sealed class Trainer: Screen() {
        @Serializable data object TrainerProfile : Trainer()
    }

    @Serializable sealed class Analytics: Screen() {
        @Serializable data object AnalyticsView : Analytics()
    }

    @Serializable sealed class Chat: Screen() {
        @Serializable data object ChatList : Chat()
    }
}