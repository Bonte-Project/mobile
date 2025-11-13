package ua.nure.bonte.ui.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ua.nure.bonte.R
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.compose.BonteBackHeader
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPassword

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Register.Event.OnBack -> navController.navigateUp()
                is Register.Event.OnNavigate -> navController.navigate(route = it.route)
                Register.Event.OnRegistrationSuccess -> navController.navigate(Screen.Profile.Dashboard) {
                    popUpTo(Screen.Profile.Dashboard) { inclusive = true }
                }
            }
        }
    }

    LaunchedEffect(state.showVerificationSheet, state.showSuccessSheet) {
        if (!state.showVerificationSheet && !state.showSuccessSheet && sheetState.isVisible) {
            scope.launch { sheetState.hide() }
        } else if ((state.showVerificationSheet || state.showSuccessSheet) && !sheetState.isVisible) {
            scope.launch { sheetState.show() }
        }
    }

    RegisterScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

    if (state.showVerificationSheet || state.showSuccessSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                if (state.inProgress) return@ModalBottomSheet
                viewModel.onAction(Register.Action.OnBack)
            },
            sheetState = sheetState,
            containerColor = AppTheme.color.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.showSuccessSheet) {
                VerificationSuccessSheetContent(state, viewModel::onAction)
            } else if (state.showVerificationSheet) {
                VerificationCodeSheetContent(state, viewModel::onAction)
            }
        }
    }

}

@Composable
private fun RegisterScreenContent(
    state: Register.State,
    onAction: (Register.Action) -> Unit
) {
    BonteScreen() {
        BonteBackHeader(
            text = stringResource(R.string.createAccount),
            onBackClick = { onAction(Register.Action.OnBack)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.firstName),
                value = state.firstName
            ) {
                onAction(Register.Action.OnFirstNameChange(firstName = it))
            }
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.lastName),
                value = state.lastName
            ) {
                onAction(Register.Action.OnLastNameChange(lastName = it))
            }
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.email),
                value = state.email
            ) {
                onAction(Register.Action.OnEmailChange(email = it))
            }
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.password),
                value = state.password,
                isPassword = true
            ) {
                onAction(Register.Action.OnPasswordChange(password = it))
            }
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.confirmPassword),
                value = state.confirmPassword,
                isPassword = true,
                errorText = state.passwordMismatchError
            ) {
                onAction(Register.Action.OnConfirmPasswordChange(confirmPassword = it))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.small)
                    .clickable{onAction(Register.Action.OnPrivacyPolicyAgreementChange(isAgreed = !state.isPrivacyPolicyAgreed))},
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = state.isPrivacyPolicyAgreed,
                    onCheckedChange = { isChecked ->
                        onAction(Register.Action.OnPrivacyPolicyAgreementChange(isAgreed = isChecked))
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = AppTheme.color.active,
                        uncheckedColor = AppTheme.color.active
                    ),
                    modifier = Modifier.padding(end = AppTheme.dimension.extraSmall)
                )
                Text(
                    text = stringResource(R.string.privacyPolicyAgreement),
                    style = AppTheme.typography.regular.copy(
                        textAlign = TextAlign.Start
                    )
                )
            }

        }
        BonteButton(
            modifier = Modifier.padding(top = 70.dp),
            textModifier = Modifier.padding(horizontal = AppTheme.dimension.normal),
            text = stringResource(R.string.continueBut),
            enabled = state.isPrivacyPolicyAgreed,
        ) {
            onAction(Register.Action.OnRegister)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = AppTheme.dimension.small)
                .padding(top = 120.dp)
                .clickable {
                    onAction(Register.Action.OnNavigate(Screen.Auth.SignIn))
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.padding(end = AppTheme.dimension.small),
                text = stringResource(R.string.haveAnAcc),
                style = AppTheme.typography.regular
            )
            Text(
                text = stringResource(R.string.login),
                style = AppTheme.typography.regular.copy(
                    color = AppTheme.color.active
                ),
            )
        }

    }
}

@Composable
private fun VerificationCodeSheetContent(
    state: Register.State,
    onAction: (Register.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimension.normal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.accountVerification),
            style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
        )

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.codemes1))
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = AppTheme.color.active)) {
                    append(state.email)
                }
                append(stringResource(R.string.codemes2))
            },
            textAlign = TextAlign.Center,
            style = AppTheme.typography.regular,
            modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
        )

        BonteInputField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
                .padding(top = AppTheme.dimension.normal),
            label = stringResource(R.string.codemes2),
            value = state.verificationCode,
            errorText = state.verificationError,
            onValueChange = { onAction(Register.Action.OnVerificationCodeChange(it)) }
        )

        Row(
            modifier = Modifier.padding(top = AppTheme.dimension.small)
        ) {
            Text(
                text = stringResource(R.string.stillNoCode),
                style = AppTheme.typography.small
            )
            Text(
                text = stringResource(R.string.resendCode),
                style = AppTheme.typography.small.copy(color = AppTheme.color.active),
                modifier = Modifier
                    .padding(bottom = AppTheme.dimension.normal)
                    .clickable { onAction(Register.Action.OnResendCodeClick) }
            )
        }

        BonteButton(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = AppTheme.dimension.normal),
            textModifier = Modifier.padding(vertical = AppTheme.dimension.small),
            text = if (state.inProgress) stringResource(R.string.verifying) else stringResource(R.string.confirm),
            enabled = state.verificationCode.isNotBlank() && !state.inProgress
        ) {
            onAction(Register.Action.OnVerificationConfirmed)
        }
    }
}

@Composable
private fun VerificationSuccessSheetContent(
    state: Register.State,
    onAction: (Register.Action) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppTheme.dimension.normal),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.successfulVerification),
            style = AppTheme.typography.large.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        BonteButton(
            modifier = Modifier.fillMaxWidth()
                .padding(bottom = AppTheme.dimension.normal),
            textModifier = Modifier.padding(vertical = AppTheme.dimension.small),
            text = if (state.inProgress) stringResource(R.string.creatingAccount) else stringResource(R.string.createAccount),
            enabled = !state.inProgress
        ) {
            onAction(Register.Action.OnVerificationSuccessAcknowledge)
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun RegisterScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            RegisterScreenContent(
                state = Register.State()
            ) { }
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegisterScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            RegisterScreenContent(
                state = Register.State()
            ) { }
        }
    }
}