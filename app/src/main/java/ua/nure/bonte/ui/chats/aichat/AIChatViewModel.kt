package ua.nure.bonte.ui.chats.aichat

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ua.nure.bonte.repository.ai.AIRepository
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AIChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val aiRepository: AIRepository
) : ViewModel() {

    private val chatId: String = savedStateHandle.get<String>("chatId") ?: ""

    private val _state = MutableStateFlow(AIChat.State())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AIChat.State()
    )

    private val _event = MutableSharedFlow<AIChat.Event>()
    val event = _event.asSharedFlow()

    init {
        observeChatHistory()
    }

    fun onAction(action: AIChat.Action) = viewModelScope.launch {
        when (action) {
            AIChat.Action.OnBack -> _event.emit(AIChat.Event.OnBack)
            is AIChat.Action.OnSendMessage -> sendMessage(action.text)
        }
    }

    private fun observeChatHistory() = viewModelScope.launch {
        val conversation = aiRepository.getOrCreateLocalConversation(chatId)!!

        aiRepository.observeVisibleHistory(conversation.id).collect { entities ->
            val messages = entities.map {
                AIChat.Message(
                    id = it.id,
                    text = it.message,
                    isFromUser = it.toFrom
                )
            }

            _state.update { current ->
                current.copy(
                    chatName = if (chatId == "ai_assistant") "AI Chat" else "Trainer Chat",
                    messages = messages
                )
            }
        }
    }
    private fun sendMessage(text: String) = viewModelScope.launch {
        val response = aiRepository.sendMessage(chatId, text)
    }
}