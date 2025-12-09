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
import ua.nure.bonte.repository.messages.MessagesRepository
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.ui.chats.chatlist.ChatList.Event.*
import javax.inject.Inject



@HiltViewModel
class ChatListViewModel @Inject constructor(
    private val aiRepository: AIRepository,
    private val messagesRepository: MessagesRepository,
    private val trainerRepository: TrainerRepository,
    private val userRepository: UserRepository
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
            ChatList.Action.OnBack -> _event.emit(ChatList.Event.OnBack)
            is ChatList.Action.OnChatClick -> {
                val route = when (action.chatType) {
                    ChatList.ChatType.AI_ASSISTANT ->
                        Screen.Chat.AIChatDetail(action.chatId)

                    ChatList.ChatType.TRAINER ->
                        Screen.Chat.TrainerChat(action.chatId)
                }

                _event.emit(ChatList.Event.OnNavigate(route))
            }
        }
    }

    private fun loadChats() = viewModelScope.launch {
        _state.update { it.copy(inProgress = true) }

        val chats = mutableListOf<ChatList.ChatItem>()
        val aiConv = aiRepository.getOrCreateLocalConversation("ai_assistant")
        aiConv?.let { conv ->
            val lastMessage = aiRepository.getVisibleHistoryOnce(conv.id)
                .messages.lastOrNull()?.message ?: ""
            chats += ChatList.ChatItem(
                id = conv.id,
                name = "AI Assistant",
                type = ChatList.ChatType.AI_ASSISTANT,
                lastMessage = lastMessage,
                avatarUrl = null
            )
        }

        val partnerIds = messagesRepository.getChatList()

        partnerIds.forEach { partnerId ->
            var name = partnerId
            var avatar: String? = null
            var type = ChatList.ChatType.TRAINER
            val userResult = userRepository.getUserById(partnerId)
            if (userResult is ua.nure.bonte.repository.Result.Success) {
                val user = userResult.data.user
                name = user.fullName ?: "User"
                avatar = user.avatarUrl
                type = ChatList.ChatType.TRAINER
            } else {
                val trainerResult = trainerRepository.getTrainerById(partnerId)
                if (trainerResult is ua.nure.bonte.repository.Result.Success) {
                    val trainer = trainerResult.data.trainer
                    val profile = userRepository.getUserById(trainer.userId)
                    name = if (profile is ua.nure.bonte.repository.Result.Success)
                        profile.data.user.fullName ?: "Trainer"
                    else "Trainer"
                    avatar = if (profile is ua.nure.bonte.repository.Result.Success)
                        profile.data.user.avatarUrl
                    else null
                    type = ChatList.ChatType.TRAINER
                }
            }

            val messages = try {
                messagesRepository.getChatHistory(partnerId)
            } catch (_: Exception) {
                emptyList()
            }

            val lastMessage = messages.lastOrNull()?.message

            chats += ChatList.ChatItem(
                id = partnerId,
                name = name,
                type = type,
                lastMessage = lastMessage,
                avatarUrl = avatar,
            )
        }
        _state.update { it.copy(chats = chats, inProgress = false) }
    }

}
