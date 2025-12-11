package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmDeleteDialog(
    modifier: Modifier = Modifier,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        Text(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.dimension.normal,
                    vertical = AppTheme.dimension.normal
                )
                .fillMaxWidth(),
            text = title,
            style = AppTheme.typography.large.copy(
                textAlign = TextAlign.Center
            )
        )
        Text(
            modifier = Modifier.padding(
                horizontal = AppTheme.dimension.normal,
                vertical = AppTheme.dimension.normal
            )
                .fillMaxWidth(),
            text = message,
            style = AppTheme.typography.regular.copy(
                textAlign = TextAlign.Center
            )
        )

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
                text = stringResource(R.string.confirm)
            ) {
                onConfirm()
            }
        }
    }
}

@Preview
@Composable
fun ConfirmDeleteDialogPreview(modifier: Modifier = Modifier) {
    AppTheme {
        ConfirmDeleteDialog(
            title = stringResource(R.string.confirmDelete),
            message = stringResource(R.string.confirmDeleteMessage),
            onConfirm = {},
            onDismiss = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun ConfirmDeleteDialogDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        ConfirmDeleteDialog(
            title = stringResource(R.string.confirmDelete),
            message = stringResource(R.string.confirmDeleteMessage),
            onConfirm = {},
            onDismiss = {}
        )
    }
}