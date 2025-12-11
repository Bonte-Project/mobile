package ua.nure.bonte.ui.trainer.own

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kizitonwose.calendar.compose.VerticalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import ua.nure.bonte.R
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.ui.compose.BonteAddSessionDialog
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteDashboardMainInfo
import ua.nure.bonte.ui.compose.BonteExperienceCard
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteHeaderType
import ua.nure.bonte.ui.compose.BonteInfoCard
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.compose.BonteSelectUsersDialog
import ua.nure.bonte.ui.compose.BonteTimePicker
import ua.nure.bonte.ui.compose.Day
import ua.nure.bonte.ui.compose.WeekdaysHeader
import ua.nure.bonte.ui.theme.AppTheme
import ua.nure.bonte.ui.trainer.view.Trainer
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun OwnTrainerScreen(
    viewModel: OwnTrainerViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
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
                        onBackClick = { onAction(OwnTrainer.Action.OnBack) },
                        onSettingsClick = { onAction(OwnTrainer.Action.OnNavigate(Screen.Trainer.EditTrainer)) }
                    )

                    BonteDashboardMainInfo(
                        modifier = Modifier.padding(bottom = AppTheme.dimension.normal),
                        name = trainerData.profile?.fullName ?: "",
                        role = trainerData.profile?.role ?: "",
                        avatarUrl = trainerData.profile?.avatarUrl,
                        onSettingsClick = {},
                    )
                    BonteInfoCard(
                        label = stringResource(R.string.bio),
                        value = trainerData.trainerEntity.bio ?: ""
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(
                        label = stringResource(R.string.certification),
                        value = trainerData.trainerEntity.certification ?: ""
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(
                        label = stringResource(R.string.specialization),
                        value = trainerData.trainerEntity.specialization ?: ""
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(
                        label = stringResource(R.string.location),
                        value = trainerData.trainerEntity.location ?: ""
                    )
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
                                    sessions = state.sessions?.get(calendarDay.date.dayOfYear),
                                    isEditEnabled = true,
                                    onDayClick = {
                                        onAction(OwnTrainer.Action.OnDayClick(date = calendarDay.date))
                                    },
                                    onEditClick = {
                                        onAction(
                                            OwnTrainer.Action.OnNavigate(
                                                route = Screen.OwnTrainer
                                                    .EditSessions(
                                                        trainerId = state.trainerId ?: "",
                                                        day = calendarDay.date.toEpochDay()
                                                    )
                                            )
                                        )
                                    }
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

        } ?: run {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                BonteButton(
                    text = stringResource(R.string.create)
                ) {
                    onAction(OwnTrainer.Action.OnCreateTrainer)
                }
            }
        }

        if (state.showAddSessionDialog) {
            BonteAddSessionDialog(
                sessionName = state.sessionName ?: "",
                date = state.selectedDay,
                time = state.selectedTime,
                userName = state.selectedUser?.fullName,
                userAvatar = state.selectedUser?.avatarUrl,
                onSessionNameChanged = {
                    onAction(OwnTrainer.Action.OnSessionNameChanged(name = it))
                },
                onApply = {
                    onAction(OwnTrainer.Action.OnCreateSession)
                },
                onDismiss = {
                    onAction(OwnTrainer.Action.OnDismissAddSessionDialog)
                },
                onTimeSelect = {
                    onAction(OwnTrainer.Action.OnShowSelectTimeDialog)
                },
                onUserSelect = {
                    onAction(OwnTrainer.Action.OnShowUserSelectDialog)
                }
            )
        }

        if (state.showSelectTimeDialog) {
            BonteTimePicker(
                onApply = { hour, minute ->
                    onAction(OwnTrainer.Action.OnSelectTime(h = hour, m = minute))
                },
                onDismiss = {
                    onAction(OwnTrainer.Action.OnDismissSelectTimeDialog)
                }
            )
        }

        if (state.showSelectUserDialog) {
            BonteSelectUsersDialog(
                items = state.users,
                onDismiss = {
                    onAction(OwnTrainer.Action.OnUserSelectDialogDismiss)
                },
                onUserSelect = { user ->
                    onAction(OwnTrainer.Action.OnUserSelect(user = user))
                },
                onLoadUser = { id ->
                    onAction(OwnTrainer.Action.OnLoadUser(userId = id))
                }
            )
        }
    }
}

