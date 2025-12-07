package ua.nure.bonte.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.content.res.Configuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepLogDialog(
    onDismiss: () -> Unit,
    onSave: (startTime: String, endTime: String, quality: Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        var startInput by remember { mutableStateOf("10:00 PM") }
        var endInput by remember { mutableStateOf("6:00 AM") }
        var quality by remember { mutableFloatStateOf(8f) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(bottom = AppTheme.dimension.normal)
        ) {
            Text(
                text = stringResource(R.string.sleep_duration),
                style = AppTheme.typography.large,
                modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimension.normal),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.start_time),
                        style = AppTheme.typography.small
                    )
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "",
                        value = startInput
                    ) { startInput = it }
                }

                Spacer(modifier = Modifier.padding(horizontal = AppTheme.dimension.small))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.end_time),
                        style = AppTheme.typography.small
                    )
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth(),
                        label = "",
                        value = endInput
                    ) { endInput = it }
                }
            }

            Text(
                text = stringResource(R.string.sleep_quality),
                style = AppTheme.typography.regular,
                modifier = Modifier.padding(bottom = AppTheme.dimension.small)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.rate_your_sleep_quality),
                    style = AppTheme.typography.small,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = quality.toInt().toString(),
                    style = AppTheme.typography.large
                )
            }

            Slider(
                value = quality,
                onValueChange = { quality = it },
                valueRange = 1f..10f,
                steps = 8,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = AppTheme.dimension.normal),
                colors = SliderDefaults.colors(
                    thumbColor = AppTheme.color.active,
                    activeTrackColor = AppTheme.color.active,
                    inactiveTrackColor = AppTheme.color.active.copy(alpha = 0.3f),
                    activeTickColor = AppTheme.color.accent,
                    inactiveTickColor = AppTheme.color.active
                )
            )

            BonteButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.save)
            ) {
                val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
                val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

                try {
                    val startTime = LocalTime.parse(startInput, timeFormatter)
                    var endTime = LocalTime.parse(endInput, timeFormatter)

                    val today = LocalDate.now()
                    var startDateTime = LocalDateTime.of(today, startTime)
                    var endDateTime = LocalDateTime.of(today, endTime)

                    if (endDateTime.isBefore(startDateTime)) {
                        endDateTime = endDateTime.plusDays(1)
                    }

                    onSave(
                        startDateTime.format(isoFormatter),
                        endDateTime.format(isoFormatter),
                        quality.toInt()
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                onDismiss()
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SleepLogDialogPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.background)
        ) {
            SleepLogDialog(
                onDismiss = {},
                onSave = { _, _, _ -> }
            )
        }
    }
}
