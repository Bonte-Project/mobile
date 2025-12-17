package ua.nure.bonte.ui.profile.dashboard

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import io.ktor.websocket.Frame
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.NutritionGoalRequest
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.ui.compose.*

import ua.nure.bonte.ui.theme.AppTheme
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

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

    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }

    val calendarState = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    fun isToday(date: String): Boolean {
        return try {
            LocalDate.parse(date.substring(0, 10)) == LocalDate.now()
        } catch (_: Exception) {
            false
        }
    }

    val todayNutritionLogs =
        state.nutritionLogs.filter { it.createdAt != null && isToday(it.createdAt) }
    val todayActivityLogs =
        state.activityLogs.filter { it.completedAt != null && isToday(it.completedAt) }
    val todaySleepLogs =
        state.sleepLogs.filter {
            isSleepForToday(it.startTime, it.endTime)
        }

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
                name = state.profile?.profileEntity?.fullName ?: "",
                role = state.profile?.profileEntity?.role ?: "",
                avatarUrl = state.profile?.profileEntity?.avatarUrl,
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

                item {
                    Column() {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = AppTheme.dimension.normal),
                            text = calendarState
                                .firstVisibleMonth
                                .yearMonth
                                .format(DateTimeFormatter.ofPattern("yyyy")),
                            style = AppTheme.typography.large.copy(
                                textAlign = TextAlign.Center
                            )
                        )
                        VerticalCalendar(
                            modifier = Modifier.height(500.dp),
                            state = calendarState,
                            dayContent = { calendarDay ->
                                Day(
                                    day = calendarDay,
                                    sessions = state.sessions?.get(calendarDay.date.dayOfYear),
                                    onDayClick = { },
                                    onEditClick = { }
                                )
                            },
                            monthHeader = { month ->
                                val daysOfWeek: List<DayOfWeek> =
                                    month.weekDays.first().map { it.date.dayOfWeek }
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = AppTheme.dimension.normal),
                                        text = month.yearMonth.format(DateTimeFormatter.ofPattern("MMMM")),
                                        style = AppTheme.typography.regular.copy(
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                    WeekdaysHeader(daysOfWeek = daysOfWeek)
                                }
                            },
                        )
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

fun isSleepForToday(start: String?, end: String?): Boolean {
    return try {
        if (end == null) return false
        val endDate = LocalDate.parse(end.substring(0, 10))
        endDate == LocalDate.now()
    } catch (_: Exception) {
        false
    }
}



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

@Preview(showBackground = true)
@Composable
private fun TooltipPreview(
    modifier: Modifier = Modifier,
    sessions: List<SessionEntity> = SessionEntity.preview
) {
    Column(
        modifier = Modifier
            .size(width = 150.dp, height = 200.dp),
    ) {

        sessions.take(15).forEach {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(4.dp))
                    .background(
                        color = when (it.status) {
                            SessionStatus.scheduled -> AppTheme.color.active
                            SessionStatus.completed -> AppTheme.color.grey
                            SessionStatus.cancelled -> Color.Red
                        },
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = AppTheme.dimension.small)
                ,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(vertical = AppTheme.dimension.small),
                    text = it.scheduledAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = AppTheme.typography.regular
                )
                Text(
                    modifier = Modifier
                        .padding(start = AppTheme.dimension.normal)
                        .weight(1F),
                    text = it.name,
                    style = AppTheme.typography.regular
                )
            }
        }

    }

}
