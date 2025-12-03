package ua.nure.bonte.ui.profile.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.compose.BonteDashboardMainInfo
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteHeaderType
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Dashboard.Event.OnBack -> navController.navigateUp()
                is Dashboard.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    DashboardScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun DashboardScreenContent(
    state: Dashboard.State,
    onAction: (Dashboard.Action) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BonteScreen {
            BonteHeader(
                text = stringResource(R.string.dashboard),
                type = BonteHeaderType.Settings,
                onSettingsClick = {
                    onAction(Dashboard.Action.OnNavigate(Screen.Profile.Settings))
                }
            )

            BonteDashboardMainInfo(
                name = state.profile?.fullName ?: "",
                role = state.profile?.role ?: "",
                avatarUrl = state.profile?.avatarUrl,
                onSettingsClick = { onAction(Dashboard.Action.OnNavigate(Screen.Profile.Settings)) },
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimension.normal),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
            ) {

            }
        }

        FloatingActionButton(
            onClick = { onAction(Dashboard.Action.OnAddButtonClick) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(AppTheme.dimension.normal),
            shape = CircleShape,
            containerColor = AppTheme.color.active
        ) {
            Text(
                text = "+",
                style = AppTheme.typography.large,
                color = AppTheme.color.background
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun DashboardScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = Modifier.background(color = AppTheme.color.background)
        ) {
            DashboardScreenContent(
                state = Dashboard.State(
                    profile = ProfileEntity.profilePreview
                ),
                onAction = { }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun DashboardScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = Modifier.background(color = AppTheme.color.background)
        ) {
            DashboardScreenContent(
                state = Dashboard.State(
                    profile = ProfileEntity.profilePreview
                ),
                onAction = { }
            )
        }
    }
}