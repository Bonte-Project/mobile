package ua.nure.bonte.ui.profile.settings

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import ua.nure.bonte.App
import ua.nure.bonte.R
import ua.nure.bonte.extension.firstName
import ua.nure.bonte.extension.lastName
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteChangeAvatarDialog
import ua.nure.bonte.ui.compose.BonteHeader
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
) {
    var headerVisibility by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0
        }.distinctUntilChanged()
            .collect {
                headerVisibility = !it
            }
    }

    BonteScreen {
        BonteHeader(
            text = stringResource(R.string.settings),
            onBackClick = {
                onAction(Settings.Action.OnBack)
            }
        )
        AnimatedVisibility(
            visible = headerVisibility
        ) {
            Box(
                modifier = Modifier.padding(all = AppTheme.dimension.normal),
                contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(shape = CircleShape)
                        .border(width = 1.dp, color = AppTheme.color.grey, shape = CircleShape),
                    model = state.profile?.avatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
                Icon(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(shape = CircleShape)
                        .background(color = AppTheme.color.active)
                        .padding(2.dp)
                        .clickable {
                            onAction(Settings.Action.OnShowChangeAvatarDialog)
                        },
                    painter = painterResource(R.drawable.edit_icon),
                    tint = AppTheme.color.background,
                    contentDescription = null
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(space = AppTheme.dimension.normal)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .clip(AppTheme.shape.accentShape)
                        .border(
                            width = 2.dp,
                            color = AppTheme.color.grey,
                            shape = AppTheme.shape.accentShape
                        ),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.personalInformation),
                        style = AppTheme.typography.large,
                        modifier = Modifier
                            .padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.small)
                            .padding(start = AppTheme.dimension.normal)
                    )
                    BonteInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal),
                        label = stringResource(R.string.firstName),
                        value = state.profile?.fullName.firstName() ?: ""
                    ) {
                        onAction(Settings.Action.OnFirstNameChange(firstName = it))
                    }
                    BonteInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.lastName),
                        value = state.profile?.fullName.lastName() ?: ""
                    ) {
                        onAction(Settings.Action.OnLastNameChange(lastName = it))
                    }
                    BonteInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.normal),
                        label = stringResource(R.string.email),
                        value = state.profile?.email ?: ""
                    ) {
                        onAction(Settings.Action.OnEmailChange(email = it))
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .clip(AppTheme.shape.accentShape)
                        .border(
                            width = 2.dp,
                            color = AppTheme.color.grey,
                            shape = AppTheme.shape.accentShape
                        ),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.healthDetails),
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
                        value = state.profile?.age?.let { it.toString() } ?: ""
                    ) {
                        onAction(Settings.Action.OnAgeChange(age = runCatching { it.toInt() }.getOrNull()))
                    }
                    BonteInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.weight),
                        value = state.profile?.weight?.let { it.toString()} ?: ""
                    ) {
                        onAction(Settings.Action.OnWeightChange(weight = runCatching { it.toInt()}.getOrNull()))
                    }
                    BonteInputField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.normal),
                        label = stringResource(R.string.height),
                        value = state.profile?.height?.let { it.toString()} ?: ""
                    ) {
                        onAction(Settings.Action.OnHeightChange(height = runCatching { it.toInt()}.getOrNull()))
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimension.normal)
                        .clip(shape = AppTheme.shape.accentShape)
                        .border(
                            width = 2.dp,
                            color = AppTheme.color.grey,
                            shape = AppTheme.shape.accentShape
                        )
                        .clickable {
                            onAction(Settings.Action.OnSubscription)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(start = AppTheme.dimension.normal)
                            .padding(vertical = AppTheme.dimension.normal),
                        painter = painterResource(R.drawable.subscription),
                        contentDescription = null,
                        tint = AppTheme.color.foreground
                    )
                    Text(
                        modifier = Modifier.padding(start = AppTheme.dimension.small),
                        text = stringResource(R.string.subscriptionPlan),
                        style = AppTheme.typography.large
                    )
                    Spacer(
                        modifier = Modifier.weight(1F)
                    )
                    Icon(
                        modifier = Modifier.padding(end = AppTheme.dimension.normal),
                        painter = painterResource(R.drawable.arrow_right),
                        contentDescription = null,
                        tint = AppTheme.color.foreground
                    )
                }
            }
            item {
                BonteButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal),
                    text = stringResource(R.string.logOut)
                ) {
                    onAction(Settings.Action.OnLogOut)
                }
            }
            item {
                Spacer(
                    modifier = Modifier.height(200.dp)
                )
            }
        }

        if (state.showChangeAvatarDialog) {
            BonteChangeAvatarDialog(
                avatar = state.profile?.avatarUrl,
                onDismiss = {
                    onAction(Settings.Action.OnDismissChangeAvatarDialog)
                },
                onAvatarChange = {
                    onAction(Settings.Action.OnAvatarChange(avatarUrl = it))
                }
            )
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