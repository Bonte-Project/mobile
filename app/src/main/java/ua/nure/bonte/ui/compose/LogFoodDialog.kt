package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogFoodDialog(
    mealType: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onLog: (
        eatenAt: String,
        mealType: String,
        name: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        grams: Int,
    ) -> Unit,
) {

    var foodName by remember { mutableStateOf("") }
    var grams by remember { mutableStateOf("") }
    var energy by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fiber by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }

    var eatenAtDate by remember {
        mutableStateOf(
            convertMillisToISO(
                LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            )
        )
    }
    val isFormValid = foodName.isNotBlank() && grams.isNotBlank() && energy.isNotBlank()
    val todayEndOfDayMillis = LocalDate.now()
        .plusDays(1)
        .atStartOfDay(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli() - 1


    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal)
        ) {
            Text(
                text = stringResource(R.string.logFood) + " (${mealType})",
                style = AppTheme.typography.large,
                modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
            )

            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                item {
                    DatePickerInputField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.eatenAt),
                        value = eatenAtDate,
                        maxDateMillis = todayEndOfDayMillis,
                        onValueChange = { eatenAtDate = it }
                    )
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.foodName),
                        value = foodName
                    ) { foodName = it }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BonteInputField(
                            modifier = Modifier.weight(1f).padding(end = AppTheme.dimension.small),
                            label = stringResource(R.string.grams),
                            value = grams
                        ) { grams = it }

                        BonteInputField(
                            modifier = Modifier.weight(1f),
                            label = stringResource(R.string.energyKcal),
                            value = energy
                        ) { energy = it }
                    }

                    Spacer(modifier = Modifier.height(AppTheme.dimension.normal))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(AppTheme.color.accent, shape = MaterialTheme.shapes.medium)
                            .padding(AppTheme.dimension.normal)
                    ) {
                        Text(text = stringResource(R.string.macronutrients), style = AppTheme.typography.regular)

                        Row(modifier = Modifier.fillMaxWidth().padding(top = AppTheme.dimension.normal), horizontalArrangement = Arrangement.SpaceBetween) {
                            BonteInputField(modifier = Modifier.weight(1f)
                                .padding(end = AppTheme.dimension.small),
                                label = stringResource(R.string.fats), value = fats) { fats = it }
                            BonteInputField(modifier = Modifier.weight(1f),
                                label = stringResource(R.string.carbs), value = carbs) { carbs = it }
                        }

                        Row(modifier = Modifier.fillMaxWidth()
                            .padding(top = AppTheme.dimension.small)
                            .padding(bottom = AppTheme.dimension.normal),
                            horizontalArrangement = Arrangement.SpaceBetween) {
                            BonteInputField(modifier = Modifier.weight(1f)
                                .padding(end = AppTheme.dimension.small),
                                label = stringResource(R.string.fiber), value = fiber) { fiber = it }
                            BonteInputField(modifier = Modifier.weight(1f),
                                label = stringResource(R.string.protein), value = protein) { protein = it }
                        }
                    }
                }
            }

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimension.normal),
                text = stringResource(R.string.logFood),
                enabled = isFormValid
            ) {
                onLog(
                    eatenAtDate,
                    mealType,
                    foodName,
                    energy.toIntOrNull() ?: 0,
                    protein.toIntOrNull() ?: 0,
                    carbs.toIntOrNull() ?: 0,
                    fats.toIntOrNull() ?: 0,
                    grams.toIntOrNull() ?: 0,
                )
                onDismiss()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LogFoodDialogPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize().background(AppTheme.color.background)) {
            LogFoodDialog(
                mealType = "Breakfast",
                onDismiss = {},
                onLog = { _, _, _, _, _, _, _, _ -> }
            )
        }
    }
}

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LogFoodDialogDarkPreview() {
    AppTheme {
        Box(modifier = Modifier.fillMaxSize().background(AppTheme.color.background)) {
            LogFoodDialog(
                mealType = "Breakfast",
                onDismiss = {},
                onLog = { _, _, _, _, _, _, _, _ -> }
            )
        }
    }
}