package ua.nure.bonte.ui.profile.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteActivityCard
import ua.nure.bonte.ui.compose.BonteDashboardMainInfo
import ua.nure.bonte.ui.compose.BonteMetricCard
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.compose.BonteRecommendationCard
import ua.nure.bonte.ui.theme.AppTheme
import androidx.compose.foundation.layout.Arrangement
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPassword
import ua.nure.bonte.ui.auth.register.Register
import ua.nure.bonte.ui.compose.BonteBackHeader

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
){
    BonteScreen(modifier = Modifier.padding(horizontal = 0.dp)) {

        BonteBackHeader(
            text = stringResource(R.string.dashboard),
            showBackButton = false,
            onBackClick = {},
            onSettingsClick = {
                onAction(Dashboard.Action.OnNavigate(Screen.Profile.Settings))
            }
        )

        BonteDashboardMainInfo(
            name = state.name,
            role = state.role,
            onEditClick = { /* TODO: onAction(Dashboard.Action.OnNavigate(Screen.EditProfile)) */ },
            onSettingsClick = { onAction(Dashboard.Action.OnNavigate(Screen.Profile.Settings)) },
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimension.normal),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
        ) {

            item {
                Text(
                    text = stringResource(R.string.todaysSummary),
                    style = AppTheme.typography.large,
                    modifier = Modifier.padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.small)
                )
            }

            item {
                BonteMetricCard(
                    title = stringResource(R.string.calories),
                    value = state.calories,
                    backgroundColor = AppTheme.color.accent,
                    onClick = {
                        //onAction(Dashboard.Action.OnNavigate(Screen.CaloriesDetails))
                    }
                )
            }
            item {
                BonteMetricCard(
                    title = stringResource(R.string.sleep),
                    value = state.sleep,
                    backgroundColor = AppTheme.color.accent,
                    onClick = {
                    // onAction(Dashboard.Action.OnNavigate(Screen.SleepDetails))
                    }
                )
            }
            item {
                BonteMetricCard(
                    title = stringResource(R.string.activity),
                    value = state.activity,
                    backgroundColor = AppTheme.color.accent,
                    onClick = {
                    // onAction(Dashboard.Action.OnNavigate(Screen.ActivityDetails))
                    }
                )
            }
            item {
                Text(
                    text = stringResource(R.string.recentActivities),
                    style = AppTheme.typography.large,
                    modifier = Modifier.padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.small)
                )
            }

            item {
                BonteActivityCard(
                    iconRes = R.drawable.nutrition_icon,
                    title = stringResource(R.string.nutrition),
                    value = state.nutrition,
                    onClick = {
                    // onAction(Dashboard.Action.OnNavigate(Screen.NutritionDetails))
                    }
                )
            }
            item {
                BonteActivityCard(
                    iconRes = R.drawable.yoga_icon,
                    title = stringResource(R.string.yoga),
                    value = state.yoga,
                    onClick = {
                    // onAction(Dashboard.Action.OnNavigate(Screen.YogaDetails))
                    }
                )
            }

            item {
                Text(
                    text = stringResource(R.string.aiInsights),
                    style = AppTheme.typography.large,
                    modifier = Modifier.padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.small)
                )
            }

            item {
                BonteMetricCard(
                    title = stringResource(R.string.sleepQuality),
                    value = state.sleepQuality,
                    backgroundColor = AppTheme.color.accent,
                    onClick = {
                    // onAction(Dashboard.Action.OnNavigate(Screen.SleepQualityDetails))
                })
            }

            item {
                BonteRecommendationCard(
                    title = "Personal Recommendations",
                    recommendationText = state.recommendations,
                    onClick = {
                    // onAction(Dashboard.Action.OnNavigate(Screen.Recommendations))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DashboardScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            DashboardScreenContent(
                state = Dashboard.State(name = "John Smith"),
                onAction = { }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DashboardScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            DashboardScreenContent(
                state = Dashboard.State(name = "John Smith"),
                onAction = { }
            )
        }
    }
}