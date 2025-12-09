package ua.nure.bonte.ui.trainer.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.ui.compose.*
import ua.nure.bonte.ui.theme.AppTheme
import ua.nure.bonte.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.ui.profile.dashboard.Dashboard
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private fun convertISOToMillis(isoString: String): Long? {
    return try {
        val localDate = LocalDate.parse(isoString.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE)
        localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (e: Exception) {
        null
    }
}

@Composable
fun TrainerScreen(
    viewModel: TrainerViewModel,
    navController: NavController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                Trainer.Event.OnBack -> navController.navigateUp()
                is Trainer.Event.OnNavigate -> navController.navigate(route = it.route)
                is Trainer.Event.OnError -> {}
            }
        }
    }

    TrainerScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun TrainerScreenContent(
    state: Trainer.State,
    onAction: (Trainer.Action) -> Unit,
) {
    BonteScreen {
        state.trainer?.let { trainerData ->
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

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimension.normal)
            ) {
                item {
                    BonteHeader(
                        text = stringResource(R.string.trainerProfile),
                        type = BonteHeaderType.Settings,
                        onBackClick = { onAction(Trainer.Action.OnBack) },
                        onSettingsClick = { onAction(Trainer.Action.OnNavigate(Screen.Trainer.EditTrainer)) }
                    )

                    BonteDashboardMainInfo(
                        modifier = Modifier.padding(bottom = AppTheme.dimension.normal),
                        name = trainerData.profile?.fullName ?: "",
                        role = trainerData.profile?.role ?: "",
                        avatarUrl = trainerData.profile?.avatarUrl,
                        onSettingsClick = {},
                    )
                    BonteInfoCard(label = stringResource(R.string.bio), value = trainerData.trainerEntity.bio ?: "")
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(label = stringResource(R.string.certification), value = trainerData.trainerEntity.certification ?: "")
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(label = stringResource(R.string.specialization), value = trainerData.trainerEntity.specialization ?: "")
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(label = stringResource(R.string.location), value = trainerData.trainerEntity.location ?: "")
                }
                itemsIndexed(trainerData.experience ?: emptyList()) { _, item ->
                    val expRequest = ExperienceRequest(
                        title = item.title ?: "",
                        description = item.description ?: "",
                        startDate = item.startDate ?: "",
                        endDate = item.endDate ?: ""
                    )

                    BonteExperienceCard(
                        exp = expRequest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppTheme.dimension.small)
                    )
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
                                    sessions = state.sessions?.get(calendarDay.date.dayOfYear)
                                ) {
                                    onAction(Trainer.Action.OnDayClick(date = calendarDay.date))
                                }
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
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.dimension.small)) {
        Text(
            text = label,
            style = AppTheme.typography.regular,
            color = AppTheme.color.foreground
        )
        Text(
            text = value,
            style = AppTheme.typography.regular,
            color = AppTheme.color.foreground
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun TrainerScreenPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background).fillMaxSize()
        ) {
            TrainerScreenContent(
                state = Trainer.State(),
                onAction = {},
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainerScreenDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background).fillMaxSize()
        ) {
            TrainerScreenContent(
                state = Trainer.State(),
                onAction = {},
            )
        }
    }
}
