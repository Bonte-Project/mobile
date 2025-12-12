package ua.nure.bonte.ui.trainer.edit

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.*
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun EditTrainerScreen(
    viewModel: EditTrainerViewModel,
    navController: NavController,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                EditTrainer.Event.OnBack -> navController.navigateUp()
                is EditTrainer.Event.OnNavigate -> navController.navigate(event.route)
            }
        }
    }

    EditTrainerScreenContent(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun EditTrainerScreenContent(
    state: EditTrainer.State,
    onAction: (EditTrainer.Action) -> Unit
) {
    var headerVisibility by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 }
            .distinctUntilChanged()
            .collect { headerVisibility = !it }
    }

    BonteScreen {
        BonteHeader(
            text = stringResource(R.string.editTrainer),
            onBackClick = { onAction(EditTrainer.Action.OnBack) }
        )

        AnimatedVisibility(visible = headerVisibility) {}

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.normal)
        ) {
            // Personal info
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .clip(AppTheme.shape.accentShape)
                        .border(2.dp, AppTheme.color.grey, AppTheme.shape.accentShape),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.personalInformation),
                        style = AppTheme.typography.large,
                        modifier = Modifier.padding(
                            top = AppTheme.dimension.normal,
                            bottom = AppTheme.dimension.small,
                            start = AppTheme.dimension.normal
                        )
                    )

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimension.normal),
                        label = stringResource(R.string.bio),
                        value = state.trainer?.bio ?: ""
                    ) { onAction(EditTrainer.Action.OnBioChange(it)) }

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.certification),
                        value = state.trainer?.certification ?: ""
                    ) { onAction(EditTrainer.Action.OnCertificationChange(it)) }

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.specialization),
                        value = state.trainer?.specialization ?: ""
                    ) { onAction(EditTrainer.Action.OnSpecializationChange(it)) }

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = AppTheme.dimension.normal)
                            .padding(vertical = AppTheme.dimension.normal),
                        label = stringResource(R.string.location),
                        value = state.trainer?.location ?: ""
                    ) { onAction(EditTrainer.Action.OnLocationChange(it)) }
                }
            }
            item {
                BonteButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal),
                    text = stringResource(R.string.confirm),
                    onClick = {
                        onAction(EditTrainer.Action.OnConfirm)
                        onAction(EditTrainer.Action.OnBack)
                    }
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .clip(AppTheme.shape.accentShape)
                        .border(2.dp, AppTheme.color.grey, AppTheme.shape.accentShape),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(R.string.experience),
                        style = AppTheme.typography.large,
                        modifier = Modifier.padding(
                            top = AppTheme.dimension.normal,
                            bottom = AppTheme.dimension.normal,
                            start = AppTheme.dimension.normal
                        )
                    )

                    if (state.experiences.isEmpty()) {
                        Text(
                            text = stringResource(R.string.noExperience),
                            style = AppTheme.typography.regular,
                            modifier = Modifier.padding(start = AppTheme.dimension.normal, bottom = AppTheme.dimension.normal)
                        )
                    }
                }
            }

            items(state.experiences, key = { it.experienceId }) { exp ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .clip(AppTheme.shape.accentShape)
                        .border(1.dp, AppTheme.color.grey, AppTheme.shape.accentShape)
                        .padding(AppTheme.dimension.normal)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = exp.title ?: stringResource(R.string.untitled),
                            style = AppTheme.typography.regular)
                        IconButton(onClick = { onAction(EditTrainer.Action.OnExperienceDelete(exp.experienceId)) }) {
                            Icon(painter = painterResource(R.drawable.delete), contentDescription = stringResource(R.string.delete), tint = AppTheme.color.foreground)
                        }
                    }

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.small),
                        label = stringResource(R.string.title),
                        value = exp.title ?: ""
                    ) { onAction(EditTrainer.Action.OnExperienceTitleChange(exp.experienceId, it)) }

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.normal, bottom = AppTheme.dimension.normal),
                        label = stringResource(R.string.experienceDescription),
                        value = exp.description ?: ""
                    ) { onAction(EditTrainer.Action.OnExperienceDescriptionChange(exp.experienceId, it)) }
                }
            }

            item {
                BonteButton(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimension.normal),
                    text = stringResource(R.string.addExperience),
                    onClick = { onAction(EditTrainer.Action.OnAddExperienceClick) }
                )
            }
        }

        if (state.showAddExperienceDialog) {
            BonteAddExperienceDialog(
                onDismiss = {
                    onAction(EditTrainer.Action.OnDismissAddExperienceDialog)
                },
                onAdd = {(title, description, startDate, endDate) ->
                    onAction(EditTrainer.Action.OnSaveExperience(title, description, startDate, endDate))
                }
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun EditTrainerScreenContentPreview() {
    AppTheme {
        EditTrainerScreenContent(state = EditTrainer.State(), onAction = {})
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun EditTrainerScreenContentDarkPreview() {
    AppTheme {
        EditTrainerScreenContent(state = EditTrainer.State(), onAction = {})
    }
}
