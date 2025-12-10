package ua.nure.bonte.ui.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.ui.compose.*
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDateTime

@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel, navController: NavController) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.onAction(Analytics.Action.Refresh)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is Analytics.Event.OnNavigate -> navController.navigate(it.route)
                Analytics.Event.OnBack -> navController.navigateUp()
            }
        }
    }

    AnalyticsScreenContent(state, viewModel::onAction)
}

@Composable
private fun AnalyticsScreenContent(
    state: Analytics.State,
    onAction: (Analytics.Action) -> Unit
) {
    BonteScreen {
        BonteHeader(
            text = "Overview",
            type = BonteHeaderType.Back,
            onBackClick = { onAction(Analytics.Action.OnBack) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimension.normal),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.normal)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
                ) {
                    Text(
                        text = "Sleep pattern",
                        style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold),
                        color = AppTheme.color.foreground
                    )

                    BonteInfoCard(
                        label = "Average sleep time",
                        value = buildString {
                            append(String.format("%.1f", state.averageSleepTime))
                            append("\n")
                            append("This week ")
                            if (state.sleepChangePercent != 0) {
                                append("${if (state.sleepChangePercent > 0) "+" else ""}${state.sleepChangePercent}%")
                            }
                        }
                    )

                    if (state.weekSleepData.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            BonteVerticalColumnGraph(
                                values = state.weekSleepData
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
                ) {
                    Text(
                        text = "Activity Levels",
                        style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold),
                        color = AppTheme.color.foreground
                    )

                    BonteInfoCard(
                        label = "Average activity time",
                        value = buildString {
                            append("${state.averageActivityTime} min")
                            append("\n")
                            append("This week ")
                            if (state.activityChangePercent != 0) {
                                append("${if (state.activityChangePercent > 0) "+" else ""}${state.activityChangePercent}%")
                            }
                        }
                    )

                    if (state.weekActivityData.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            BonteVerticalColumnGraph(
                                values = state.weekActivityData
                            )
                        }
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
                ) {
                    Text(
                        text = "Nutrition",
                        style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold),
                        color = AppTheme.color.foreground
                    )

                    BonteInfoCard(
                        label = "Average calories",
                        value = buildString {
                            append("${state.averageCalories} kcal")
                            append("\n")
                            append("This week ")
                            if (state.caloriesChangePercent != 0) {
                                append("${if (state.caloriesChangePercent > 0) "+" else ""}${state.caloriesChangePercent}%")
                            }
                        }
                    )

                    if (state.weekNutritionData.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            BonteVerticalColumnGraph(
                                values = state.weekNutritionData
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun AnalyticsPreview() {
    AppTheme {
        AnalyticsScreenContent(
            state = Analytics.State(
                averageSleepTime = 7.5,
                sleepChangePercent = -5,
                averageActivityTime = 45,
                activityChangePercent = 15,
                averageCalories = 2200,
                caloriesChangePercent = 10,
                weekSleepData = listOf(
                    Triple(8, "Mon", LocalDateTime.now().minusDays(6)),
                    Triple(7, "Tue", LocalDateTime.now().minusDays(5)),
                    Triple(9, "Wed", LocalDateTime.now().minusDays(4)),
                    Triple(6, "Thu", LocalDateTime.now().minusDays(3)),
                    Triple(8, "Fri", LocalDateTime.now().minusDays(2)),
                    Triple(7, "Sat", LocalDateTime.now().minusDays(1)),
                    Triple(8, "Sun", LocalDateTime.now())
                ),
                weekActivityData = listOf(
                    Triple(6, "Mon", LocalDateTime.now().minusDays(6)),
                    Triple(5, "Tue", LocalDateTime.now().minusDays(5)),
                    Triple(3, "Wed", LocalDateTime.now().minusDays(4)),
                    Triple(5, "Thu", LocalDateTime.now().minusDays(3)),
                    Triple(4, "Fri", LocalDateTime.now().minusDays(2)),
                    Triple(4, "Sat", LocalDateTime.now().minusDays(1)),
                    Triple(6, "Sun", LocalDateTime.now())
                ),
                weekNutritionData = listOf(
                    Triple(2000, "Mon", LocalDateTime.now().minusDays(6)),
                    Triple(2100, "Tue", LocalDateTime.now().minusDays(5)),
                    Triple(2200, "Wed", LocalDateTime.now().minusDays(4)),
                    Triple(1900, "Thu", LocalDateTime.now().minusDays(3)),
                    Triple(2000, "Fri", LocalDateTime.now().minusDays(2)),
                    Triple(2300, "Sat", LocalDateTime.now().minusDays(1)),
                    Triple(2200, "Sun", LocalDateTime.now())
                )
            ),
            onAction = {}
        )
    }
}
