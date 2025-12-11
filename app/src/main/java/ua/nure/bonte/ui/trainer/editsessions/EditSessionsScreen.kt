package ua.nure.bonte.ui.trainer.editsessions

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.kizitonwose.calendar.core.daysOfWeek
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.ui.compose.BonteAddSessionDialog
import ua.nure.bonte.ui.compose.BonteEditSessionDialog
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.compose.BonteTimePicker
import ua.nure.bonte.ui.compose.ConfirmDeleteDialog
import ua.nure.bonte.ui.compose.convertMillisToISO
import ua.nure.bonte.ui.theme.AppTheme
import ua.nure.bonte.ui.trainer.composable.SessionEditItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun EditSessionsScreen(
    viewModel: EditSessionsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                EditSessions.Event.OnBack -> navController.navigateUp()
                is EditSessions.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    EditSessionsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSessionsScreenContent(
    state: EditSessions.State,
    onAction: (EditSessions.Action) -> Unit
) {
    BonteScreen {
        BonteHeader(
            text = stringResource(R.string.editSessions),
            onBackClick = {
                onAction(EditSessions.Action.OnBack)
            }
        )

        state.day?.let { day ->
            Text(
                modifier = Modifier
                    .padding(vertical = AppTheme.dimension.normal)
                    .fillMaxWidth()
                ,
                text = day.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                style = AppTheme.typography.large.copy(
                    textAlign = TextAlign.Center
                )
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1F)
                .padding(horizontal = AppTheme.dimension.normal),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            state.sessions?.let { sessions ->
                items(items = sessions, key = { it.id }) { session ->
                    SessionEditItem(
                        modifier = Modifier,
                        item = session,
                        onEdit = {
                            onAction(EditSessions.Action.OnShowEditSessionDialog(sessionId = session.id))
                        },
                        onDelete = {
                            onAction(EditSessions.Action.OnShowConfirmDeleteDialog(sessionId = session.id))
                        },
                        onStatusChange = { session ->
                            onAction(EditSessions.Action.OnStatusChange(session = session))
                        }
                    )
                }
            }

        }

        if(state.showEditSessionDialog) {
            BonteEditSessionDialog(
                sessionName = state.editedSession?.name ?: "",
                date = state.editedSession?.scheduledAt?.toLocalDate() ?: LocalDate.now(),
                time = state.editedSession?.scheduledAt?.toLocalTime() ?: LocalTime.now(),
                onSessionNameChanged = {
                    onAction(EditSessions.Action.OnSessionNameChanged(name = it))
                },
                onApply = {
                    onAction(EditSessions.Action.OnEditSessionConfirmed)
                },
                onDismiss = {
                    onAction(EditSessions.Action.OnDismissEditSessionDialog)
                },
                onTimeSelect = {
                    onAction(EditSessions.Action.OnShowSelectTimeDialog)
                },
                onDateSelect = {
                    onAction(EditSessions.Action.OnShowSelectDateDialog)
                }
            )

        }

        if(state.showTimeSelectDialog) {
            BonteTimePicker(
                onApply = { hour, min ->
                    onAction(EditSessions.Action.OnTimeSelect(hour = hour, min = min))
                },
                onDismiss = {
                    onAction(EditSessions.Action.OnDismissSelectTimeDialog)
                }
            )
        }

        if(state.showDateSelectDialog) {
            val state = rememberDatePickerState(
                initialSelectedDate = state.editedSession?.scheduledAt?.toLocalDate() ?: LocalDate.now()
            )
            DatePickerDialog(
                onDismissRequest = {
                    onAction(EditSessions.Action.OnDismissSelectDateDialog)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAction(EditSessions.Action.OnDateSelect(date = state.getSelectedDate()))
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.confirm),
                            style = AppTheme.typography.regular
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { onAction(EditSessions.Action.OnDismissSelectDateDialog)}
                    ) {
                        Text(
                            text =stringResource(R.string.cancel),
                            style = AppTheme.typography.regular
                        )
                    }
                }
            ) {
                DatePicker(
                    state = state
                )
            }
        }

        if(state.showConfirmDeleteDialog) {
            ConfirmDeleteDialog(
                title = stringResource(R.string.confirmDelete),
                message = stringResource(R.string.confirmDeleteMessage),
                onConfirm = {
                    onAction(EditSessions.Action.OnDeleteSession)
                },
                onDismiss = {
                    onAction(EditSessions.Action.OnDismissConfirmDeleteDialog)
                }
            )
        }

    }
}

@Preview(showSystemUi = true)
@Composable
private fun EditSessionsScreenPreview(modifier: Modifier = Modifier) {
    AppTheme {
        EditSessionsScreenContent(
            state = EditSessions.State(
                sessions = SessionEntity.preview,
                day = LocalDate.now(),
            )
        ) {}
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun EditSessionsScreenDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        EditSessionsScreenContent(
            state = EditSessions.State(
                sessions = SessionEntity.preview,
                day = LocalDate.now(),
            )
        ) { }
    }
}