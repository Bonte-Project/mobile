package ua.nure.bonte.ui.chats.aichat

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteHeaderType
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun AIChatScreen(
    viewModel: AIChatViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                AIChat.Event.OnBack -> navController.navigateUp()
                is AIChat.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    ChatScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ChatScreenContent(
    state: AIChat.State,
    onAction: (AIChat.Action) -> Unit
) {
    var messageText by remember { mutableStateOf("") }

    BonteScreen {
        Column(modifier = Modifier.fillMaxSize()) {
            BonteHeader(
                text = state.chatName,
                type = BonteHeaderType.Back,
                onBackClick = {
                    onAction(AIChat.Action.OnBack)
                }
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal),
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small),
                reverseLayout = true
            ) {
                items(state.messages.reversed()) { message ->
                    MessageItem(message = message)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppTheme.dimension.normal),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BonteInputField(
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.type_message),
                    value = messageText,
                    onValueChange = { messageText = it }
                )

                Spacer(modifier = Modifier.width(AppTheme.dimension.small))

                IconButton(
                    onClick = {
                        if (messageText.isNotBlank()) {
                            onAction(AIChat.Action.OnSendMessage(messageText))
                            messageText = ""
                        }
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(AppTheme.color.active)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = stringResource(R.string.send),
                        tint = AppTheme.color.background
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageItem(
    message: AIChat.Message
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isFromUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(AppTheme.color.accent),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_ai_avatar),
                    contentDescription = "AI Avatar",
                    modifier = Modifier
                        .size(24.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(AppTheme.dimension.small))
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    if (message.isFromUser) AppTheme.color.accent
                    else AppTheme.color.background
                )
                .padding(AppTheme.dimension.normal)
        ) {
            Column {
                if (!message.isFromUser) {
                    Text(
                        text = "AI Assistant",
                        style = AppTheme.typography.small,
                        color = AppTheme.color.active
                    )
                }
                Text(
                    text = message.text,
                    style = AppTheme.typography.regular
                )
            }
        }


    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            ChatScreenContent(
                state = AIChat.State(
                    chatName = "AI Chat",
                    messages = listOf(
                        AIChat.Message(
                            id = "1",
                            text = "Hello! How can I help you?",
                            isFromUser = false
                        ),
                        AIChat.Message(
                            id = "2",
                            text = "Hi! I need help with my workout plan",
                            isFromUser = true
                        ),
                        AIChat.Message(
                            id = "3",
                            text = "I'd be happy to help! What are your fitness goals?",
                            isFromUser = false
                        )
                    )
                ),
                onAction = { }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChatScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            ChatScreenContent(
                state = AIChat.State(
                    chatName = "AI Chat",
                    messages = listOf(
                        AIChat.Message(
                            id = "1",
                            text = "Hello! How can I help you?",
                            isFromUser = false
                        ),
                        AIChat.Message(
                            id = "2",
                            text = "Hi! I need help with my workout plan",
                            isFromUser = true
                        ),
                        AIChat.Message(
                            id = "3",
                            text = "I'd be happy to help! What are your fitness goals?",
                            isFromUser = false
                        )
                    )
                ),
                onAction = { }
            )
        }
    }
}