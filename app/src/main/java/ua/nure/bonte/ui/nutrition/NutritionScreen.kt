package ua.nure.bonte.ui.nutrition

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nure.bonte.R
import ua.nure.bonte.ui.compose.BonteButton
import ua.nure.bonte.ui.compose.BonteHeader
import ua.nure.bonte.ui.compose.BonteScreen
import ua.nure.bonte.ui.compose.LogFoodDialog
import ua.nure.bonte.ui.theme.AppTheme
import androidx.annotation.StringRes
import ua.nure.bonte.navigation.Screen
import ua.nure.bonte.ui.addmenu.AddMenu
import ua.nure.bonte.ui.compose.BonteAddGoalDialog
import ua.nure.bonte.ui.compose.SleepLogDialog

@Composable
fun NutritionScreen(
    viewModel: NutritionViewModel,
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.event.collect {
            when(it) {
                Nutrition.Event.OnBack -> navController.navigateUp()
                is Nutrition.Event.OnNavigate -> navController.navigate(route = it.route)
                is Nutrition.Event.OnError -> {}
                is Nutrition.Event.ShowSuccess -> {}
            }
        }
    }
    NutritionScreenContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun NutritionScreenContent(
    state: Nutrition.State,
    onAction: (Nutrition.Action) -> Unit,
) {

    var selectedMealType by remember { mutableStateOf<String?>(null) }


    BonteScreen {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimension.normal)
        ) {
            BonteHeader(
                text = stringResource(R.string.addFood),
                onBackClick = { onAction(Nutrition.Action.OnBack) }
            )

            @Composable
            fun MealTypeButton(mealType: String, @StringRes stringRes: Int) {
                BonteButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = AppTheme.dimension.small),
                    text = stringResource(stringRes),
                ) {
                    selectedMealType = mealType
                }
            }

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimension.small),
                text = stringResource(R.string.updateGoal),
            ) {
                onAction(Nutrition.Action.OnUpdateGoalClick)
            }
            MealTypeButton("Breakfast", R.string.breakfast)
            MealTypeButton("Lunch", R.string.lunch)
            MealTypeButton("Dinner", R.string.dinner)
            MealTypeButton("Snack", R.string.snack)
        }
        if (state.showAddGoalDialog) {
            BonteAddGoalDialog(
                onDismiss = { onAction(Nutrition.Action.OnDismissAddGoalDialog) },
                onLog = { calories, protein, carbs, fat ->
                    onAction(Nutrition.Action.OnSaveGoal(calories, protein, carbs, fat))
                }
            )
        }
        selectedMealType?.let { mealType ->
            LogFoodDialog(
                mealType = mealType,
                onDismiss = { selectedMealType = null },
                onLog = { date, mType, name, kcal, protein, carbs, fat,grams ->
                    onAction(Nutrition.Action.OnSaveNutritionLog(date,mType,  name, kcal, protein, carbs, fat,grams  ))
                    selectedMealType = null
                }
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun NutritionScreenContentPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        Box(
            modifier = modifier.background(color = AppTheme.color.background)
        ) {
            NutritionScreenContent(
                state = Nutrition.State(),
                onAction = {},
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
            NutritionScreenContent(
                state = Nutrition.State(),
                onAction = {},
            )
        }
    }
}