package ua.nure.bonte.navigation

import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.chatGraph(navController: NavController) {
    navigation<NestedGraph.Chat>(
        startDestination = Screen.Chat.ChatList
    ) {
        composable<Screen.Chat.ChatList> {
            Text(
                text = "Chat list"
            )
        }
    }
}