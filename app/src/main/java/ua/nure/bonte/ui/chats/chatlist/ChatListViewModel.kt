package ua.nure.bonte.ui.chats.chatlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.ai.AIRepository
import ua.nure.bonte.ui.chats.chatlist.ChatList.Event.*
import javax.inject.Inject

@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val aiRepository: AIRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(ChatList.State())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ChatList.State()
    )

    private val _event = MutableSharedFlow<ChatList.Event>()
    val event = _event.asSharedFlow()

    init {
        loadChats()
    }

    fun onAction(action: ChatList.Action) = viewModelScope.launch {
        when (action) {
            ChatList.Action.OnBack -> {
                _event.emit(OnBack)
            }

            is ChatList.Action.OnChatClick -> {
                _event.emit(OnNavigate(route = Screen.Chat.AIChatDetail(action.chatId)))
            }
        }
    }

    private fun loadChats() = viewModelScope.launch {
        val chats = mutableListOf<ChatList.ChatItem>()

        val aiConversation = aiRepository.getOrCreateLocalConversation("ai_assistant")

        aiConversation?.let { conv ->
            val lastMsgEntity = aiRepository.observeChatHistory(conv.id)
                .stateIn(viewModelScope)
                .value
                .lastOrNull()?.message ?: ""

            chats += ChatList.ChatItem(
                id = conv.id,
                name = "AI Assistant",
                type = ChatList.ChatType.AI_ASSISTANT,
                lastMessage = lastMsgEntity,
                avatarUrl = null,
                unreadCount = 0
            )
        }

        _state.update { it.copy(chats = chats) }
    }


}