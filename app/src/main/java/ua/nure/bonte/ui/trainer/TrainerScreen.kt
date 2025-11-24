package ua.nure.bonte.ui.trainer

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.ui.compose.*
import ua.nure.bonte.ui.theme.AppTheme
import ua.nure.bonte.R
import androidx.compose.ui.res.stringResource
import ua.nure.bonte.repository.dto.ExperienceRequest
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
// Додаємо необхідні імпорти для DatePicker logic
import java.time.ZoneId
import java.time.LocalDate
import java.time.Instant

private fun convertISOToMillis(isoString: String): Long? {
    return try {
        val localDate = LocalDate.parse(isoString.substring(0, 10), java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
        localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (e: Exception) {
        null
    }
}


@Composable
fun BonteInfoField(modifier: Modifier = Modifier, label: String, value: String) {
    BonteInputField(modifier = modifier, label = label, value = value, onValueChange = {})
}

@Composable
fun TrainerScreen(
    viewModel: TrainerViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                Trainer.Event.OnBack -> navController.navigateUp()
                is Trainer.Event.OnNavigate -> navController.navigate(route = it.route)
                is Trainer.Event.OnError -> {}
            }
        }
    }

    TrainerScreenContent(
        state = state,
        onAction = viewModel::onAction,
        onCreateTrainer = viewModel::createTrainer
    )
}

@Composable
private fun TrainerScreenContent(
    state: Trainer.State,
    onAction: (Trainer.Action) -> Unit,
    onCreateTrainer: (
        bio: String,
        certification: String,
        specialization: String,
        experience: List<ExperienceRequest>,
        location: String
    ) -> Unit
) {
    var showCreateDialog by remember { mutableStateOf(false) }
    var showAddExperienceDialog by remember { mutableStateOf(false) }

    BonteScreen {

        state.profile?.trainer?.let { trainerData ->

            val profileEntity = state.profile.profileEntity
            val trainerEntity = trainerData.trainerEntity
            val experienceList = trainerData.experience ?: emptyList()

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = AppTheme.dimension.normal)
            ) {
                item {
                    BonteHeader(
                        text = stringResource(R.string.trainerProfile),
                        onBackClick = { onAction(Trainer.Action.OnBack) }
                    )

                    BonteDashboardMainInfo(
                        name = profileEntity.fullName ?: "",
                        role = profileEntity.role,
                        avatarUrl = profileEntity.avatarUrl,
                        onSettingsClick = {},
                    )

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.bio),
                        value = trainerEntity.bio,
                        onValueChange = { onAction(Trainer.Action.OnBioChange(it)) }
                    )

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.certification),
                        value = trainerEntity.certification,
                        onValueChange = { onAction(Trainer.Action.OnCertificationChange(it)) }
                    )

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.specialization),
                        value = trainerEntity.specialization,
                        onValueChange = { onAction(Trainer.Action.OnSpecializationChange(it)) }
                    )

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.normal),
                        label = stringResource(R.string.location),
                        value = trainerEntity.location,
                        onValueChange = { onAction(Trainer.Action.OnLocationChange(it)) }
                    )
                }

                itemsIndexed(experienceList) { _, item ->
                    val minDateMillis = remember(item.startDate) {
                        item.startDate.takeIf { it.isNotBlank() }?.let { convertISOToMillis(it) }
                    }
                    val todayMillis = remember {
                        LocalDate.now()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()
                            .toEpochMilli()
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppTheme.dimension.small)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BonteInputField(
                                modifier = Modifier.weight(1f).padding(end = AppTheme.dimension.small),
                                label = stringResource(R.string.experience),
                                value = item.title,
                                onValueChange = {
                                    onAction(Trainer.Action.OnExperienceChange(
                                        item.experienceId,
                                        it,
                                        item.description,
                                        item.startDate,
                                        item.endDate
                                    ))
                                }
                            )

                            BonteButton(
                                modifier = Modifier,
                                text = "X",
                            ) {
                                onAction(Trainer.Action.OnDeleteExperience(item.experienceId))
                            }
                        }
                        BonteInputField(
                            modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.small),
                            label = stringResource(R.string.experienceDescription),
                            value = item.description,
                            onValueChange = {
                                onAction(Trainer.Action.OnExperienceChange(
                                    item.experienceId,
                                    item.title,
                                    it,
                                    item.startDate,
                                    item.endDate
                                ))
                            }
                        )

                        // 🚀 ІНТЕГРАЦІЯ DATE PICKER: startDate (змінна)
                        DatePickerInputField(
                            modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.small),
                            label = stringResource(R.string.startDate),
                            value = item.startDate,
                            onValueChange = {
                                onAction(Trainer.Action.OnExperienceChange(
                                    item.experienceId,
                                    item.title,
                                    item.description,
                                    it, // New Start Date
                                    item.endDate
                                ))
                            }
                        )

                        DatePickerInputField(
                            modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.small),
                            label = stringResource(R.string.endDate),
                            value = item.endDate,
                            minDateMillis = minDateMillis, 
                            maxDateMillis = todayMillis,
                            onValueChange = {
                                onAction(Trainer.Action.OnExperienceChange(
                                    item.experienceId,
                                    item.title,
                                    item.description,
                                    item.startDate,
                                    it // New End Date
                                ))
                            }
                        )
                    }
                }

                item {
                    BonteButton(
                        modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.dimension.normal),
                        text = stringResource(R.string.addExperience),
                    ) {
                        showAddExperienceDialog = true
                    }

                    BonteButton(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.normal),
                        text = stringResource(R.string.confirm),
                        enabled = true
                    ) {
                        onAction(Trainer.Action.OnSaveProfile)
                    }

                    BonteButton(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.normal),
                        text = stringResource(R.string.deleteProfile),
                        enabled = false
                    ) {
                    }
                }
            }

        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BonteButton(
                    text = stringResource(R.string.createTrainerProfile)
                ) {
                    showCreateDialog = true
                }
            }
        }
        if (showCreateDialog) {
            BonteTrainerCreateDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { bio, certification, specialization, experience, location ->
                    onCreateTrainer(bio, certification, specialization, experience, location)
                    showCreateDialog = false
                }
            )
        }
        if (showAddExperienceDialog) {
            BonteAddExperienceDialog(
                onDismiss = { showAddExperienceDialog = false },
                onAdd = { request ->
                    onAction(Trainer.Action.OnAddExperienceWithData(request))
                    showAddExperienceDialog = false
                }
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun TrainerScreenPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background).fillMaxSize()
        ) {
            TrainerScreenContent(
                state = Trainer.State(),
                onAction = {},
                onCreateTrainer = { _, _, _, _, _ -> }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun TrainerScreenDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(
            modifier = modifier.background(color = AppTheme.color.background).fillMaxSize()
        ) {
            TrainerScreenContent(
                state = Trainer.State(),
                onAction = {},
                onCreateTrainer = { _, _, _, _, _ -> }
            )
        }
    }
}