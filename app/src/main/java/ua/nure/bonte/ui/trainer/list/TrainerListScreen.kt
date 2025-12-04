package ua.nure.bonte.ui.trainer.list

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.nure.bonte.R
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteHeaderType
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.compose.TrainerItem
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun TrainerListScreen(
    viewModel: TrainerListViewModel,
    onBack: () -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                TrainerList.Event.OnBack -> onBack()
                is TrainerList.Event.OnNavigate -> onNavigate(it.route)
            }
        }
    }
    TrainerListContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun TrainerListContent(
    state: TrainerList.State,
    onAction: (TrainerList.Action) -> Unit
) {
    BonteScreen() {
        BonteHeader(
            text = stringResource(R.string.trainerList),
            type = BonteHeaderType.Back
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            state = rememberLazyListState()
        ) {
            items(items = state.trainers ?: emptyList(), key = {it.trainerEntity.trainerId}) {
                TrainerItem(
                    avatarUrl = it.profile?.avatarUrl,
                    fullName = it.profile?.fullName,
                    specialization = it.trainerEntity.specialization,
                    onClick = {
                        onAction(TrainerList.Action.OnNavigate(route = Screen.Trainer.TrainerProfile))
                    },
                    onLoadProfile = {
                        onAction(TrainerList.Action.OnLoadProfile(profileId = it.trainerEntity.userId))
                    }
                )
            }

        }
    }
}


@Preview
@Composable
private fun TrainerListContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(modifier = Modifier.background(AppTheme.color.background)) {
            TrainerListContent(
                state = TrainerList.State(),
                onAction = {}
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainerListContentPreviewDark(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(modifier = Modifier.background(AppTheme.color.background)) {
            TrainerListContent(
                state = TrainerList.State(),
                onAction = {}
            )
        }
    }
}