package ua.nure.bonte.ui.chats.aichat

import ua.nure.bonte.navigation.Screen

object AIChat {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnSendMessage(val text: String) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val messages: List<Message> = emptyList(),
        val chatName: String = "",
        val chatAvatarUrl: String? = null
    )

    data class Message(
        val id: String,
        val text: String,
        val isFromUser: Boolean,
        val timestamp: Long = System.currentTimeMillis(),
        val avatarUrl: String? = null
    )
}