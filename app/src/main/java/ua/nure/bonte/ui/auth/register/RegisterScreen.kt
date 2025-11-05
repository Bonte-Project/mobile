package ua.nure.bonte.ui.auth.register

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.ui.theme.AppTheme

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
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Button(
            onClick = {
                onAction(Register.Action.OnRegister)
            }
        ) {
            Text(
                text = "Register"
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