package ua.nure.bonte.ui.chats.trainerchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ua.nure.bonte.db.data.entity.MessageEntity
import ua.nure.bonte.repository.messages.MessagesRepository
import ua.nure.bonte.repository.trainer.TrainerRepository
import ua.nure.bonte.repository.user.UserRepository
import ua.nure.bonte.repository.Result
import javax.inject.Inject

@HiltViewModel
class TrainerChatViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val trainerRepository: TrainerRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TrainerChat.State())
    val state: StateFlow<TrainerChat.State> = _state

    private val _event = MutableSharedFlow<TrainerChat.Event>()
    val event = _event.asSharedFlow()
    private var targetIsUser: Boolean = true

    fun onAction(action: TrainerChat.Action) {
        when (action) {
            is TrainerChat.Action.Load -> loadChat(action.conversationId, action.title)
            is TrainerChat.Action.OnSend -> send(action.text)
            TrainerChat.Action.OnBack -> viewModelScope.launch { _event.emit(TrainerChat.Event.OnBack) }
            else -> Unit
        }
    }

    private fun loadChat(conversationId: String, title: String?) {
        _state.update {
            it.copy(
                inProgress = true,
                conversationId = conversationId,
                chatName = title ?: "Chat"
            )
        }

        viewModelScope.launch {
            try {
                val myTrainerResult = trainerRepository.loadMyTrainer()
                val amITrainer = myTrainerResult is Result.Success
                println(">>> DEBUG: amITrainer=$amITrainer")

                val actualConversationId = if (amITrainer) {
                    val userResult = userRepository.getUserById(conversationId)
                    if (userResult is Result.Success) {
                        val userName = userResult.data.user.fullName ?: "User"
                        _state.update { it.copy(chatName = userName) }
                        println(">>> DEBUG: chatName set to userName=$userName")
                    }
                    targetIsUser = true
                    conversationId
                } else {
                    val trainerResult = trainerRepository.getTrainerById(conversationId)
                    if (trainerResult is Result.Success) {
                        val profileResult = userRepository.getUserById(trainerResult.data.trainer.userId)
                        val trainerName = if (profileResult is Result.Success)
                            profileResult.data.user.fullName ?: "Trainer"
                        else "Trainer"
                        _state.update { it.copy(chatName = trainerName) }
                        println(">>> DEBUG: chatName set to trainerName=$trainerName")
                    }
                    targetIsUser = false
                    conversationId
                }
                val messages = messagesRepository.getChatHistory(actualConversationId)
                println(">>> DEBUG: messagesRepository.getChatHistory returned ${messages.size} messages")
                messages.forEach { println(">>> DEBUG MSG: id=${it.id}, message=${it.message}, to_from=${it.to_from}") }

                val uiMessages = messages.map { dto ->
                    TrainerChat.Message(
                        id = dto.id,
                        text = dto.message,
                        isFromUser = !dto.to_from,
                        sentAt = dto.sent_at
                    )
                }.sortedBy { it.sentAt }

                _state.update { it.copy(messages = uiMessages, inProgress = false) }
                messagesRepository.observeChatHistory(actualConversationId).collect { entities ->
                    println(">>> DEBUG OBSERVE: ${entities.size} entities from DB")
                    val liveMessages = entities.map { entity: MessageEntity ->
                        TrainerChat.Message(
                            id = entity.id,
                            text = entity.message,
                            isFromUser = !entity.fromTrainer,
                            sentAt = entity.createdAt
                        )
                    }.sortedBy { it.sentAt }
                    _state.update { it.copy(messages = liveMessages, inProgress = false) }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun send(text: String) {
        val conv = _state.value.conversationId ?: return
        val amIUser = !targetIsUser
        println(">>> DEBUG: Sending message. amIUser=$amIUser, targetIsUser=$targetIsUser")

        viewModelScope.launch {
            messagesRepository.sendMessage(
                conversationId = conv,
                message = text,
                toTrainer = amIUser // true если я юзер
            )
        }
    }
}
