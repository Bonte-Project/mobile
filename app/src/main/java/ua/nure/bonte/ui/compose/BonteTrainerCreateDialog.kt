package ua.nure.bonte.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import ua.nure.bonte.R
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteTrainerCreateDialog(
    onDismiss: () -> Unit,
    onCreate: (
        bio: String,
        certification: String,
        specialization: String,
        location: String,
        isActive: Boolean
    ) -> Unit,
) {
    var bio by remember { mutableStateOf("") }
    var certification by remember { mutableStateOf("") }
    var specialization by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = AppTheme.dimension.normal)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = AppTheme.dimension.normal),
                    text = stringResource(R.string.createTrainerProfile),
                    style = AppTheme.typography.large
                )

                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal, vertical = AppTheme.dimension.normal),
                    label = stringResource(R.string.bio),
                    value = bio
                ) { bio = it }

                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal),
                    label = stringResource(R.string.certification),
                    value = certification
                ) { certification = it }

                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal, vertical = AppTheme.dimension.normal),
                    label = stringResource(R.string.specialization),
                    value = specialization
                ) { specialization = it }
                BonteInputField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = AppTheme.dimension.normal)
                        .padding(bottom = AppTheme.dimension.normal),
                    label = stringResource(R.string.location),
                    value = location
                ) { location = it }
            }
            item {
                BonteButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = AppTheme.dimension.normal),
                    text = stringResource(R.string.create)
                ) {
                    onCreate(bio, certification, specialization, location, true)
                    onDismiss()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TrainerCreateDialogPreview() {
    AppTheme {
        BonteTrainerCreateDialog(
            onDismiss = {},
            onCreate = { _, _, _, _, _-> }
        )
    }
}