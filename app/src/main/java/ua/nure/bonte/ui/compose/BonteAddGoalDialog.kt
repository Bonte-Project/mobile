package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteAddGoalDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onLog: (
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int
    ) -> Unit,
) {
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }


    val isFormValid = calories.isNotBlank() && protein.isNotBlank() && carbs.isNotBlank() && fat.isNotBlank()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
        ) {
            Text(
                text = stringResource(R.string.updateGoal),
                style = AppTheme.typography.large,
                modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                item {
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.calories),
                        value = calories,
                    ) { calories = it }
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.protein),
                        value = protein,
                    ) { protein = it }
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.carbs),
                        value = carbs,
                    ) { carbs = it }
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.fats),
                        value = fat,
                    ) { fat = it }
                }
            }

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimension.normal),
                text = stringResource(R.string.updateGoal),
                enabled = isFormValid
            ) {
                onLog(
                    calories.toIntOrNull() ?: 0,
                    protein.toIntOrNull() ?: 0,
                    carbs.toIntOrNull() ?: 0,
                    fat.toIntOrNull() ?: 0,
                )
                onDismiss()
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun BonteAddGoalDialogPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.background)
        ) {
            BonteAddGoalDialog(
                onDismiss = {},
                onLog = { _, _, _, _,-> }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BonteAddGoalDialogDarkPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.color.background)
        ) {
            BonteAddGoalDialog(
                onDismiss = {},
                onLog = { _, _, _, _, -> }
            )
        }
    }
}