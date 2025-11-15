package ua.nure.bonte.ui.auth.forgotpassword.compose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.theme.AppTheme


private val TAG by lazy { "NewPasswordDialog" }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewPasswordDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onNewPassword: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        var password by remember {
            mutableStateOf("")
        }
        var confirmPassword by remember {
            mutableStateOf("")
        }
        Text(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimension.normal),
            text = stringResource(R.string.newPassword),
            style = AppTheme.typography.large
        )
        BonteInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(top = AppTheme.dimension.normal),
            label = stringResource(R.string.password),
            value = password,
            isPassword = true
        ) {
            password = it
        }
        BonteInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(top = AppTheme.dimension.normal),
            label = stringResource(R.string.confirmPassword),
            value = confirmPassword,
            isPassword = true
        ) {
            confirmPassword = it
        }
        BonteButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimension.normal),
            text = stringResource(R.string.confirmPassword)
        ) {
            if (password == confirmPassword) {
                Log.d(TAG, "NewPasswordDialog: match!")
                onNewPassword(password)
            } else {
                Log.d(TAG, "NewPasswordDialog: Not match: $password, $confirmPassword")
            }
        }
    }
}

@Preview
@Composable
private fun NewPasswordDialogPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        NewPasswordDialog(
            onDismiss = {},
            onNewPassword = {}
        )
    }
}