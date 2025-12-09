package ua.nure.bonte.ui.chats.trainerchat

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.*
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun TrainerChatScreen(
    viewModel: TrainerChatViewModel,
    navController: NavController,
    conversationId: String,
    chatName: String? = null
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(conversationId) {
        viewModel.onAction(TrainerChat.Action.Load(conversationId, chatName))
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                TrainerChat.Event.OnBack -> navController.navigateUp()
            }
        }
    }

    TrainerChatContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TrainerChatContent(
    state: TrainerChat.State,
    onAction: (TrainerChat.Action) -> Unit
) {
    var text by remember { mutableStateOf("") }

    BonteScreen {
        Column(Modifier.fillMaxSize()) {

            BonteHeader(
                text = state.chatName,
                type = BonteHeaderType.Back,
                onBackClick = { onAction(TrainerChat.Action.OnBack) }
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = AppTheme.dimension.normal),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
            ) {
                items(state.messages.reversed()) { message ->
                    val currentIsUser = state.profile?.trainer?.let { false } ?: run { true }
                    TrainerMessageItem(message = message, currentIsUser = currentIsUser)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimension.normal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BonteInputField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.type_message)
                )

                Spacer(modifier = Modifier.width(AppTheme.dimension.small))

                IconButton(
                    onClick = {
                        if (text.isNotBlank()) {
                            onAction(TrainerChat.Action.OnSend(text))
                            text = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AppTheme.color.active)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = null,
                        tint = AppTheme.color.background
                    )
                }
            }
        }
    }
}

@Composable
private fun TrainerMessageItem(message: TrainerChat.Message, currentIsUser: Boolean) {
    val fromMe = if (currentIsUser) {
        message.isFromUser
    } else {
        !message.isFromUser
    }

    println(">>> DEBUG MESSAGE: id=${message.id}, text='${message.text}', isFromUser=${message.isFromUser}, currentIsUser=$currentIsUser, fromMe=$fromMe")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (fromMe) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(if (fromMe) AppTheme.color.accent else AppTheme.color.background)
                .padding(AppTheme.dimension.normal)
        ) {
            Text(text = message.text, style = AppTheme.typography.regular)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TrainerChatScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            TrainerChatContent(
                state = TrainerChat.State(
                    chatName = "Trainer Chat",
                    messages = listOf(
                        TrainerChat.Message(id = "1", text = "Hello! How can I help you?", isFromUser = false, sentAt = ""),
                        TrainerChat.Message(id = "2", text = "Hi! I need help with my workout plan", isFromUser = true, sentAt = ""),
                        TrainerChat.Message(id = "3", text = "I'd be happy to help! What are your fitness goals?", isFromUser = false, sentAt = "")
                    )
                ),
                onAction = { }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainerChatScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            TrainerChatContent(
                state = TrainerChat.State(
                    chatName = "Trainer Chat",
                    messages = listOf(
                        TrainerChat.Message(id = "1", text = "Hello! How can I help you?", isFromUser = false, sentAt = ""),
                        TrainerChat.Message(id = "2", text = "Hi! I need help with my workout plan", isFromUser = true, sentAt = ""),
                        TrainerChat.Message(id = "3", text = "I'd be happy to help! What are your fitness goals?", isFromUser = false, sentAt = "")
                    )
                ),
                onAction = { }
            )
        }
    }
}
