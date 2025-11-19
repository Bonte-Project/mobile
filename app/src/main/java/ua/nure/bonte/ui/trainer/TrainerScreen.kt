package ua.nure.bonte.ui.trainer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun TrainerScreen(
    viewModel: TrainerViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                Trainer.Event.OnBack -> navController.navigateUp()
                is Trainer.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    TrainerScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TrainerScreenContent(
    state: Trainer.State,
    onAction: (Trainer.Action) -> Unit
) {
    BonteScreen {
        state.profile?.trainer?.let {
            // Trainer View Here!!!
        } ?: run {
            BonteButton(
                text = stringResource(R.string.create)
            ) {
                onAction(Trainer.Action.OnCreateTrainer)
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun TrainerScreenPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            TrainerScreenContent(
                state = Trainer.State(),
            ) { }
        }
    }

}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainerScreenDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            TrainerScreenContent(
                state = Trainer.State(),
            ) { }
        }
    }

}