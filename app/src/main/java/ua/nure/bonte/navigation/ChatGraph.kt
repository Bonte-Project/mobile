package ua.nure.bonte.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import ua.nure.bonte.ui.chats.aichat.AIChatScreen
import ua.nure.bonte.ui.chats.chatlist.ChatListScreen

fun NavGraphBuilder.chatGraph(navController: NavController) {
    navigation<NestedGraph.Chat>(
        startDestination = Screen.Chat.ChatList
    ) {
        composable<Screen.Chat.ChatList> {
            ChatListScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Chat.AIChatDetail> { backStackEntry ->
            val chatDetail = backStackEntry.toRoute<Screen.Chat.AIChatDetail>()
            AIChatScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}