package ua.nure.bonte.ui.chats.chatlist

import ua.nure.bonte.navigation.Screen

object ChatList {
    sealed interface Event {
        data class OnNavigate(val route: Screen) : Event
        data object OnBack : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnChatClick(val chatId: String, val chatType: ChatType) : Action
    }

    data class State(
        val inProgress: Boolean = false,
        val chats: List<ChatItem> = emptyList()
    )

    data class ChatItem(
        val id: String,
        val name: String,
        val type: ChatType,
        val lastMessage: String?,
        val avatarUrl: String?,
        val unreadCount: Int = 0
    )

    enum class ChatType {
        AI_ASSISTANT,
        TRAINER
    }
}