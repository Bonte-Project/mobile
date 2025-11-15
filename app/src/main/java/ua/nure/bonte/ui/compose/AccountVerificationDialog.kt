package ua.nure.bonte.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.App
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountVerificationDialog(
    modifier: Modifier = Modifier,
    email: String,
    onResend: () -> Unit,
    onVerify: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var code by remember() {
        mutableStateOf("")
    }
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimension.normal),
            text = stringResource(R.string.accountVerification),
            style = AppTheme.typography.large
        )
        val message = buildAnnotatedString {
            withStyle(AppTheme.typography.regular.toSpanStyle()) {
                append(stringResource(R.string.accountVerificationMessagePart1))
                append(" ")
            }
            withStyle(
                AppTheme.typography.regular.copy(color = AppTheme.color.accent).toSpanStyle()
            ) {
                append(email)
                append(" ")
            }
            withStyle(AppTheme.typography.regular.toSpanStyle()) {
                append(stringResource(R.string.accountVerificationMessagePart2))
                append(" ")
            }
            pushStringAnnotation(tag = "resend code", annotation = "resend code")
            withStyle(
                AppTheme.typography.regular.copy(color = AppTheme.color.accent).toSpanStyle()
            ) {
                append(stringResource(R.string.resendCode))
            }
            pop()
        }
        ClickableText(
            modifier = Modifier
                .padding(top = AppTheme.dimension.normal)
                .padding(horizontal = AppTheme.dimension.normal),
            text = message,
            onClick = {
                message.getStringAnnotations(tag = "resend code", start = it, end = it)
                    .firstOrNull()?.let {
                        onResend()
                    }
            }
        )
        BonteInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(top = AppTheme.dimension.normal),
            label = stringResource(R.string.code),
            value = code
        ) {
            code = it
        }
        BonteButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimension.normal),
            text = stringResource(R.string.verifyCode)
        ) {
            onVerify(code)
        }
    }
}

@Preview
@Composable
private fun AccountVerificationDialogPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        AccountVerificationDialog(
            modifier = Modifier,
            email = "john.dow@gamil.com",
            onResend = {},
            onVerify = {},
        ) { }
    }
}