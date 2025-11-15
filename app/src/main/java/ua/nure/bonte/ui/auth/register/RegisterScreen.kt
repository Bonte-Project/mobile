package ua.nure.bonte.ui.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import ua.nure.bonte.ui.auth.forgotpassword.ForgotPassword
import ua.nure.bonte.ui.compose.AccountVerificationDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Register.Event.OnBack -> navController.navigateUp()
                is Register.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }

    RegisterScreenContent(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
private fun RegisterScreenContent(
    state: Register.State,
    onAction: (Register.Action) -> Unit
) {
    BonteScreen() {
        BonteHeader(
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

        if(state.showVerificationDialog) {
            AccountVerificationDialog(
                modifier = Modifier,
                email = state.email,
                onResend = {
                    onAction(Register.Action.OnResendCodeClick)
                },
                onVerify = { code ->
                    onAction(Register.Action.OnVerificationEmailCode(code = code))
                },
                onDismiss = {
                    onAction(Register.Action.OnDismissEmailVerification)
                }
            )
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