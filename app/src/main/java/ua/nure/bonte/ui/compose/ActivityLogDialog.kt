package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLogDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onLog: (
        activityType: String,
        intensity: String,
        durationMinutes: Int,
        completedAt: String,
    ) -> Unit,
) {
    var activityType by remember { mutableStateOf("") }
    var intensity by remember { mutableFloatStateOf(5f) }
    var durationMinutes by remember { mutableStateOf("") }
    var completedAt by remember {
        mutableStateOf(
            convertMillisToISO(
                LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        )
    }

    val todayMillis = remember {
        LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    val isFormValid = activityType.isNotBlank() && durationMinutes.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
        ) {
            Text(
                text = stringResource(R.string.logActivity),
                style = AppTheme.typography.large,
                modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    DatePickerInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.completedAt),
                        value = completedAt,
                        maxDateMillis = todayMillis,
                        onValueChange = { completedAt = it }
                    )
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.activityType),
                        value = activityType,
                    ) { activityType = it }

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.duration),
                        value = durationMinutes,
                    ) { durationMinutes = it }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppTheme.dimension.small)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = stringResource(R.string.intensity),
                                style = AppTheme.typography.regular
                            )
                            Text(
                                modifier = Modifier.padding(end = AppTheme.dimension.small),
                                text = intensity.toInt().toString(),
                                style = AppTheme.typography.regular
                            )
                        }
                        Slider(
                            value = intensity,
                            onValueChange = { intensity = it },
                            valueRange = 1f..10f,
                            steps = 10,
                            colors = SliderDefaults.colors(
                                thumbColor = AppTheme.color.active,
                                activeTrackColor = AppTheme.color.active,
                                inactiveTrackColor = AppTheme.color.active.copy(alpha = 0.3f),
                                activeTickColor = AppTheme.color.accent,
                                inactiveTickColor = AppTheme.color.active
                            )
                        )
                    }
                }
            }

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimension.normal),
                text = stringResource(R.string.logActivity),
                enabled = isFormValid
            ) {
                onLog(
                    activityType,
                    intensity.toInt().toString(),
                    durationMinutes.toIntOrNull() ?: 0,
                    completedAt,
                )
                onDismiss()
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ActivityLogDialogPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.background)
        ) {
            ActivityLogDialog(
                onDismiss = {},
                onLog = { _, _, _, _,-> }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LogActivityDialogDarkPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.background)
        ) {
            ActivityLogDialog(
                onDismiss = {},
                onLog = { _, _, _, _, -> }
            )
        }
    }
}