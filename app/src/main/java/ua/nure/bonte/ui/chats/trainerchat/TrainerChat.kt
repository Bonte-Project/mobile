package ua.nure.bonte.ui.chats.trainerchat

import ua.nure.bonte.db.data.entity.Profile

object TrainerChat {
    sealed interface Action {
        data class Load(val conversationId: String, val title: String?) : Action
        data class OnInput(val text: String) : Action
        data class OnSend(val text: String) : Action
        object OnBack : Action
    }

    sealed interface Event {
        object OnBack : Event
    }

    data class State(
        val conversationId: String? = null,
        val chatName: String = "",
        val messages: List<Message> = emptyList(),
        val inProgress: Boolean = false,
        val profile: Profile? = null
    )

    data class Message(
        val id: String,
        val text: String,
        val isFromUser: Boolean,
        val sentAt: String
    )
}
