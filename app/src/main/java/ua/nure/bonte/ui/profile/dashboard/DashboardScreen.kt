package ua.nure.bonte.ui.profile.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.NutritionGoalRequest
import ua.nure.bonte.ui.compose.*

import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate

@Composable
fun DashboardScreen(viewModel: DashboardViewModel, navController: NavController) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.onAction(Dashboard.Action.Refresh)
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is Dashboard.Event.OnNavigate -> navController.navigate(it.route)
                Dashboard.Event.OnBack -> navController.navigateUp()
            }
        }
    }

    DashboardScreenContent(state, viewModel::onAction)
}

@Composable
private fun DashboardScreenContent(
    state: Dashboard.State,
    onAction: (Dashboard.Action) -> Unit
) {

    fun isToday(date: String): Boolean {
        return try {
            LocalDate.parse(date.substring(0, 10)) == LocalDate.now()
        } catch (_: Exception) {
            false
        }
    }

    val todayNutritionLogs = state.nutritionLogs.filter { it.createdAt != null && isToday(it.createdAt) }
    val todayActivityLogs = state.activityLogs.filter { it.completedAt != null && isToday(it.completedAt) }
    val todaySleepLogs = state.sleepLogs.filter { it.startTime != null && isToday(it.startTime) }

    val totalCalories = todayNutritionLogs.sumOf { it.calories }
    val totalProtein = todayNutritionLogs.sumOf { it.protein }
    val totalCarbs = todayNutritionLogs.sumOf { it.carbs }
    val totalFat = todayNutritionLogs.sumOf { it.fat }

    val totalActivityMinutes = todayActivityLogs.sumOf { it.durationMinutes }

    val totalSleepMinutes = todaySleepLogs.sumOf { log ->
        try {
            val start = java.time.OffsetDateTime.parse(log.startTime)
            val end = java.time.OffsetDateTime.parse(log.endTime)
            java.time.Duration.between(start, end).toMinutes().toInt()
        } catch (_: Exception) {
            0
        }
    }

    val sleepHours = totalSleepMinutes / 60
    val sleepMinutes = totalSleepMinutes % 60

    val lastMeal = todayNutritionLogs.lastOrNull() ?: state.nutritionLogs.lastOrNull()
    val lastActivity = todayActivityLogs.lastOrNull() ?: state.activityLogs.lastOrNull()

    val recentItems = buildList {
        lastMeal?.let {
            add(
                RecentItem(
                    type = "nutrition",
                    title = it.name,
                    value = "${it.calories} kcal",
                    time = it.createdAt,
                    icon = R.drawable.nutrition_icon
                )
            )
        }

        lastActivity?.let {
            val intensity = it.intensity.toIntOrNull() ?: 0
            val icon = when (intensity) {
                in 1..3 -> R.drawable.yoga_icon
                in 4..7 -> R.drawable.run_icon
                in 8..10 -> R.drawable.gym_icon
                else -> R.drawable.activity_default
            }

            add(
                RecentItem(
                    type = "activity",
                    title = it.activityType,
                    value = "${it.durationMinutes} min",
                    time = it.completedAt,
                    icon = icon
                )
            )
        }
    }

    Box(Modifier.fillMaxSize()) {

        BonteScreen {

            BonteHeader(
                text = stringResource(R.string.dashboard),
                type = BonteHeaderType.Settings,
                onSettingsClick = { onAction(Dashboard.Action.OnNavigate(Screen.Profile.Settings)) }
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
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.normal)
            ) {

                item {
                    BonteMetricCard(
                        title = "Calories",
                        value = totalCalories.toString(),
                        backgroundColor = AppTheme.color.accent,
                        onClick = { onAction(Dashboard.Action.OnNavigate(Screen.Profile.Nutrition)) }
                    )
                }

                item {
                    BonteMetricCard(
                        title = "Sleep",
                        value = "${sleepHours}h ${sleepMinutes}m",
                        backgroundColor = AppTheme.color.accent,
                    ) {}
                }

                item {
                    BonteMetricCard(
                        title = "Activity",
                        value = "${totalActivityMinutes} min",
                        backgroundColor = AppTheme.color.accent,
                    ) {}
                }

                state.goal?.let { goal ->
                    item {
                        BonteGoalCard(
                            title = "Goal Progress",
                            recommendationText =
                                """
                                Calories: $totalCalories/${goal.calories}
                                Protein: ${totalProtein}/${goal.protein}g
                                Carbs: ${totalCarbs}/${goal.carbs}g
                                Fat: ${totalFat}/${goal.fat}g
                                """.trimIndent()
                        )
                    }
                }

                if (recentItems.isNotEmpty()) {
                    item {
                        Text(
                            text = "Recent",
                            style = AppTheme.typography.regular.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(top = AppTheme.dimension.normal)
                        )
                    }

                    items(recentItems) { item ->
                        BonteActivityCard(
                            iconRes = item.icon,
                            title = item.title,
                            value = item.value,
                        ) {}
                    }
                }
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

data class RecentItem(
    val type: String,
    val title: String,
    val value: String,
    val time: String,
    val icon: Int
)

@Preview(showSystemUi = true)
@Composable
private fun DashboardPreview() {
    AppTheme {
        DashboardScreenContent(
            state = Dashboard.State(
                profile = ProfileEntity.profilePreview,
                goal = NutritionGoalRequest(2000, 100, 250, 60)
            ),
            onAction = {}
        )
    }
}
