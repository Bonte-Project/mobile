package ua.nure.bonte.ui.compose

import android.icu.util.Calendar
import android.widget.TimePicker
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.util.TableInfo
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteTimePicker(
    modifier: Modifier = Modifier,
    onApply: (Int,Int) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {

        val localTime = LocalTime.now()

        val timePickerState = rememberTimePickerState(
            initialHour = localTime.hour,
            initialMinute = localTime.minute,
            is24Hour = true
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimeInput(
                state = timePickerState
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimension.normal)
        ) {
            BonteButton(
                modifier = Modifier
                    .weight(1F)
                    .padding(
                        horizontal = AppTheme.dimension.normal,
                        vertical = AppTheme.dimension.small
                    ),
                text = stringResource(R.string.cancel)
            ) {
                onDismiss()
            }

            BonteButton(
                modifier = Modifier
                    .weight(1F)
                    .padding(
                        horizontal = AppTheme.dimension.normal,
                        vertical = AppTheme.dimension.small
                    ),
                text = stringResource(R.string.add)
            ) {
                onApply(timePickerState.hour, timePickerState.minute)
            }
        }
    }

}

@Preview
@Composable
private fun BonteTimePickerPreview(modifier: Modifier = Modifier) {
    AppTheme {
        BonteTimePicker(
            onApply = { _, _ -> },
            onDismiss = {}
        )
    }

}