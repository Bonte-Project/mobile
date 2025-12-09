package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteAddSessionDialog(
    modifier: Modifier = Modifier,
    sessionName: String,
    onSessionNameChanged: (String) -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        BonteInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal),
            label = stringResource(R.string.experience),
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
                text = stringResource(R.string.add)
            ) {
                onApply()
            }
        }
    }
}

@Preview
@Composable
fun BonteAddSessionDialogPreview(modifier: Modifier = Modifier) {
    AppTheme {
        BonteAddSessionDialog(
            sessionName = "some name",
            onApply = {},
            onSessionNameChanged = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun BonteAddSessionDialogDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        BonteAddSessionDialog(
            sessionName = "some name",
            onApply = {},
            onSessionNameChanged = {},
            onDismiss = {}
        )
    }
}