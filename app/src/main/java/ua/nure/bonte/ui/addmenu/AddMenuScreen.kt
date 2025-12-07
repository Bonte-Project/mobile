package ua.nure.bonte.ui.addmenu

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteHeaderType
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.compose.SleepLogDialog
import ua.nure.bonte.ui.compose.ActivityLogDialog
import ua.nure.bonte.ui.profile.dashboard.Dashboard
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun AddMenuScreen(
    viewModel: AddMenuViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                AddMenu.Event.OnBack -> navController.navigateUp()
                is AddMenu.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    AddMenuScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun AddMenuScreenContent(
    state: AddMenu.State,
    onAction: (AddMenu.Action) -> Unit
) {
    BonteScreen {
        BonteHeader(
            text = stringResource(R.string.add),
            type = BonteHeaderType.Back,
            onBackClick = {
                onAction(AddMenu.Action.OnBack)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(top = AppTheme.dimension.normal),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.normal)
        ) {
            Text(
                text = stringResource(R.string.what_are_you_adding),
                style = AppTheme.typography.large
            )

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                icon = R.drawable.ic_food,
                text = stringResource(R.string.add_food),
                onClick = {
                    onAction(AddMenu.Action.OnNavigate(Screen.Profile.Nutrition))
                }
            )

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                icon = R.drawable.ic_sleep,
                text = stringResource(R.string.update_sleep_log),
                onClick = {
                    onAction(AddMenu.Action.OnUpdateSleepLogClick)
                }
            )

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                icon = R.drawable.ic_activity,
                text = stringResource(R.string.update_activity_log),
                onClick = {
                    onAction(AddMenu.Action.OnUpdateActivityLogClick)
                }
            )
        }

        if (state.showSleepDialog) {
            SleepLogDialog(
                onDismiss = { onAction(AddMenu.Action.OnDismissSleepDialog) },
                onSave = { startTime, endTime, quality ->
                    onAction(AddMenu.Action.OnSaveSleepLog(startTime, endTime, quality))
                }
            )
        }
        if (state.showActivityDialog) {
            ActivityLogDialog(
                onDismiss = { onAction(AddMenu.Action.OnDismissActivityDialog) },
                onLog = {activityType, intensity, durationMinutes, completedAt ->
                    onAction(AddMenu.Action.OnSaveActivityLog(activityType, intensity, durationMinutes, completedAt))
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AddMenuScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            AddMenuScreenContent(
                state = AddMenu.State(),
                onAction = { }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddMenuScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            AddMenuScreenContent(
                state = AddMenu.State(),
                onAction = { }
            )
        }
    }
}