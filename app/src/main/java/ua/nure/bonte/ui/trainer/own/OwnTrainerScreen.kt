package ua.nure.bonte.ui.trainer.own

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.ui.compose.BonteScreen

@Composable
fun OwnTrainerScreen(
    viewModel: OwnTrainerViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                OwnTrainer.Event.OnBack -> navController.navigateUp()
                is OwnTrainer.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    OwnTrainerScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun OwnTrainerScreenContent(
    state: OwnTrainer.State,
    onAction: (OwnTrainer.Action) -> Unit
) {
    BonteScreen {
        Text(
            text = "Own Trainer Screen"
        )
    }

}