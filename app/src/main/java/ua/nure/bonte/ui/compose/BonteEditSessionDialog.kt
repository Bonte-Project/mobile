package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteEditSessionDialog(
    modifier: Modifier = Modifier,
    sessionName: String,
    date: LocalDate? = null,
    time: LocalTime? = null,
    onSessionNameChanged: (String) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
    onTimeSelect: () -> Unit,
    onDateSelect: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal, vertical = AppTheme.dimension.normal)
                .clickable(onClick = onDateSelect),
            text = date?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
            style = AppTheme.typography.regular
        )

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal, vertical = AppTheme.dimension.normal)
                .clickable(onClick = onTimeSelect),
            text = time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: stringResource(R.string.selectTime),
            style = AppTheme.typography.regular
        )

        BonteInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal, vertical = AppTheme.dimension.normal),
            label = stringResource(R.string.sessionName),
            value = sessionName
        ) {
            onSessionNameChanged(it)
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
                text = stringResource(R.string.applyTitle)
            ) {
                onApply()
            }
        }
    }
}

@Preview
@Composable
private fun BonteEditSessionDialogPreview(modifier: Modifier = Modifier) {
    AppTheme {
        BonteEditSessionDialog(
            sessionName = "some name",
            date = LocalDate.now(),
            time = LocalTime.now(),
            onApply = {},
            onSessionNameChanged = {},
            onDismiss = {},
            onTimeSelect = {},
            onDateSelect = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun BonteEditSessionDialogDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        BonteEditSessionDialog(
            sessionName = "some name",
            date = LocalDate.now(),
            time = LocalTime.now(),
            onApply = {},
            onSessionNameChanged = {},
            onDismiss = {},
            onTimeSelect = {},
            onDateSelect = {}
        )
    }
}