package ua.nure.bonte.ui.auth.forgotpassword

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                ForgotPassword.Event.OnBack -> navController.navigateUp()
                is ForgotPassword.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }
    ForgotPasswordScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun ForgotPasswordScreenContent(
    state: ForgotPassword.State,
    onAction: (ForgotPassword.Action) -> Unit
){
    BonteScreen() {
        BonteHeader(
            text = stringResource(R.string.resetPassword),
            onBackClick = {
                onAction(ForgotPassword.Action.OnBack)
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                ) {
                    Text(
                        modifier = Modifier.padding(top = AppTheme.dimension.normal),
                        text = stringResource(R.string.enterEmail),
                        style = AppTheme.typography.large
                    )
                    Text(
                        modifier = Modifier.padding(top = AppTheme.dimension.extraSmall),
                        text = stringResource(R.string.codeSendMes),
                        style = AppTheme.typography.regular
                    )
                }

                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .padding(top = AppTheme.dimension.normal)
                        .padding(top = AppTheme.dimension.normal),
                    label = stringResource(R.string.email),
                    value = state.email
                ) {
                    onAction(ForgotPassword.Action.OnEmailChange(email = it))
                }

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BonteButton(
                    modifier = Modifier.fillMaxWidth()
                        .padding(bottom = 40.dp),
                    textModifier = Modifier.padding(horizontal = AppTheme.dimension.normal),
                    text = stringResource(R.string.continueBut),
                ) {

                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ForgotPasswordScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            ForgotPasswordScreenContent(
                state = ForgotPassword.State(),
                onAction = {}
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ForgotPasswordScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            ForgotPasswordScreenContent(
                state = ForgotPassword.State(),
                onAction = {}
            )
        }
    }
}