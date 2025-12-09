package ua.nure.bonte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.nure.bonte.navigation.NavGraph
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.navigation.topLevelRoutes
import ua.nure.bonte.ui.compose.navBar.BonteBottomNavigationBar
import ua.nure.bonte.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (showBottomNavigationBar(navDestination = currentDestination)) {
                            BonteBottomNavigationBar(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = AppTheme.color.background)
                                    .navigationBarsPadding()
                                    .padding(start = AppTheme.dimension.small, end = AppTheme.dimension.small),
                                currentDestination = currentDestination,
                                items = topLevelRoutes
                            ) { nestedGraphRoute ->
                                navController.navigate(nestedGraphRoute) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = AppTheme.color.background),
                        contentAlignment = Alignment.Center
                    ) {
                        NavGraph(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            navController = navController
                        )

                    }
                }
            }
        }
    }
}

fun showBottomNavigationBar(navDestination: NavDestination?) =
    navDestination?.let { destination ->
        destination.route in listOf(
            Screen.Profile.Dashboard::class.qualifiedName,
            Screen.Profile.Settings::class.qualifiedName,
            Screen.Trainer.TrainerList::class.qualifiedName,
            Screen.Chat.ChatList::class.qualifiedName,
            Screen.Analytics.AnalyticsView::class.qualifiedName,
            Screen.OwnTrainer.View::class.qualifiedName,
        )
    } ?: false