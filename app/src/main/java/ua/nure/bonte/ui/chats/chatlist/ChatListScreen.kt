package ua.nure.bonte.ui.chats.chatlist

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import coil3.compose.AsyncImage
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteHeaderType
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun ChatListScreen(
    viewModel: ChatListViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                ChatList.Event.OnBack -> navController.navigateUp()
                is ChatList.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    ChatListScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ChatListScreenContent(
    state: ChatList.State,
    onAction: (ChatList.Action) -> Unit
) {
    BonteScreen {
        BonteHeader(
            text = stringResource(R.string.chats),
            type = BonteHeaderType.Back,
            onBackClick = { onAction(ChatList.Action.OnBack) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(top = AppTheme.dimension.normal),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
        ) {
            items(state.chats) { chat ->
                ChatListItem(
                    chat = chat,
                    onClick = { onAction(ChatList.Action.OnChatClick(chat.id, chat.type)) }
                )
            }
        }
    }
}

@Composable
private fun ChatListItem(
    chat: ChatList.ChatItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = AppTheme.color.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimension.normal),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AppTheme.color.accent),
                contentAlignment = Alignment.Center
            ) {
                when (chat.type) {
                    ChatList.ChatType.AI_ASSISTANT -> {
                        Image(
                            painter = painterResource(R.drawable.ic_ai_avatar),
                            contentDescription = "AI Avatar",
                            modifier = Modifier.size(32.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    ChatList.ChatType.TRAINER -> {
                        if (chat.avatarUrl != null) {
                            AsyncImage(
                                model = chat.avatarUrl,
                                contentDescription = "Trainer Avatar",
                                modifier = Modifier.size(48.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = chat.name.first().toString(),
                                style = AppTheme.typography.large
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(AppTheme.dimension.normal))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = chat.name,
                    style = AppTheme.typography.regular
                )
                chat.lastMessage?.let {
                    Text(
                        text = it,
                        style = AppTheme.typography.small,
                        color = AppTheme.color.grey,
                        maxLines = 1
                    )
                }
            }

            if (chat.unreadCount > 0) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(AppTheme.color.active),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = chat.unreadCount.toString(),
                        style = AppTheme.typography.small,
                        color = AppTheme.color.background
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ChatListScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            ChatListScreenContent(
                state = ChatList.State(
                    chats = listOf(
                        ChatList.ChatItem(
                            id = "ai_chat",
                            name = "AI Assistant",
                            type = ChatList.ChatType.AI_ASSISTANT,
                            lastMessage = "How can I help you today?",
                            avatarUrl = null,
                            unreadCount = 2
                        ),
                        ChatList.ChatItem(
                            id = "trainer1",
                            name = "John Trainer",
                            type = ChatList.ChatType.TRAINER,
                            lastMessage = "Great progress today!",
                            avatarUrl = null,
                            unreadCount = 0
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
private fun ChatListScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            ChatListScreenContent(
                state = ChatList.State(
                    chats = listOf(
                        ChatList.ChatItem(
                            id = "ai_chat",
                            name = "AI Assistant",
                            type = ChatList.ChatType.AI_ASSISTANT,
                            lastMessage = "How can I help you today?",
                            avatarUrl = null,
                            unreadCount = 2
                        ),
                        ChatList.ChatItem(
                            id = "trainer1",
                            name = "John Trainer",
                            type = ChatList.ChatType.TRAINER,
                            lastMessage = "Great progress today!",
                            avatarUrl = null,
                            unreadCount = 0
                        )
                    )
                ),
                onAction = { }
            )
        }
    }
}
