package ua.nure.bonte.ui.profile.settings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.ui.auth.register.Register
import ua.nure.bonte.ui.auth.signin.SignIn
import ua.nure.bonte.ui.compose.BonteBackHeader
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteInputField
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when (it) {
                Settings.Event.OnBack -> navController.navigateUp()
                is Settings.Event.OnNavigate -> navController.navigate(route = it.route)
            }
        }
    }
    SettingsScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun SettingsScreenContent(
    state: Settings.State,
    onAction: (Settings.Action) -> Unit
){
    BonteScreen(

    ) {
        BonteBackHeader(
            text = stringResource(R.string.settings)
        ) {
            onAction(Settings.Action.OnBack)
        }
        BonteButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = AppTheme.dimension.normal),
            textModifier = Modifier.padding(vertical = AppTheme.dimension.small),
            text = stringResource(R.string.changeplan)
        ) {}
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(R.string.editDetails),
                style = AppTheme.typography.large,
                modifier = Modifier
                    .padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.small)
                    .padding(start = AppTheme.dimension.normal)
            )
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal),
                label = stringResource(R.string.age),
                value = state.age
            ) {
                onAction(Settings.Action.OnAgeChange(age = it))
            }
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.weight),
                value = state.weight
            ) {
                onAction(Settings.Action.OnWeightChange(weight = it))
            }
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppTheme.dimension.normal)
                    .padding(top = AppTheme.dimension.normal),
                label = stringResource(R.string.height),
                value = state.height
            ) {
                onAction(Settings.Action.OnHeightChange(height = it))
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun SettingsScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            SettingsScreenContent(
                state = Settings.State(),
                onAction = {}
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenContentDarkPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            SettingsScreenContent(
                state = Settings.State(),
                onAction = {}
            )
        }
    }
}