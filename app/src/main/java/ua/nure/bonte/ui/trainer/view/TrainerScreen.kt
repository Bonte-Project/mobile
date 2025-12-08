package ua.nure.bonte.ui.trainer.view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
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
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.ui.profile.dashboard.Dashboard
import java.time.ZoneId
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private fun convertISOToMillis(isoString: String): Long? {
    return try {
        val localDate = LocalDate.parse(isoString.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE)
        localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (e: Exception) {
        null
    }
}

@Composable
fun TrainerScreen(
    viewModel: TrainerViewModel,
    navController: NavController,
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
    LaunchedEffect(Unit) {
        viewModel.refreshTrainer()
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
                        type = BonteHeaderType.Settings,
                        onBackClick = { onAction(Trainer.Action.OnBack) },
                        onSettingsClick = { onAction(Trainer.Action.OnNavigate(Screen.Trainer.EditTrainer)) }
                    )

                    BonteDashboardMainInfo(
                        modifier = Modifier.padding(bottom = AppTheme.dimension.normal),
                        name = profileEntity.fullName ?: "",
                        role = profileEntity.role,
                        avatarUrl = profileEntity.avatarUrl,
                        onSettingsClick = {},
                    )
                    BonteInfoCard(label = stringResource(R.string.bio), value = trainerEntity.bio ?: "")
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(label = stringResource(R.string.certification), value = trainerEntity.certification ?: "")
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(label = stringResource(R.string.specialization), value = trainerEntity.specialization ?: "")
                    Spacer(modifier = Modifier.height(AppTheme.dimension.small))
                    BonteInfoCard(label = stringResource(R.string.location), value = trainerEntity.location ?: "")
                }
                itemsIndexed(experienceList) { _, item ->
                    val expRequest = ExperienceRequest(
                        title = item.title ?: "",
                        description = item.description ?: "",
                        startDate = item.startDate ?: "",
                        endDate = item.endDate ?: ""
                    )

                    BonteExperienceCard(
                        exp = expRequest,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppTheme.dimension.small)
                    )
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
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = AppTheme.dimension.small)) {
        Text(
            text = label,
            style = AppTheme.typography.regular,
            color = AppTheme.color.foreground
        )
        Text(
            text = value,
            style = AppTheme.typography.regular,
            color = AppTheme.color.foreground
        )
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
