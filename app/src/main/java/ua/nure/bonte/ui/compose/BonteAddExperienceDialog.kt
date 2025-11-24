package ua.nure.bonte.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import ua.nure.bonte.R
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.ui.theme.AppTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import java.time.Instant
import java.time.ZoneOffset
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import ua.nure.bonte.ui.compose.BonteInputField
import androidx.compose.material3.SelectableDates

fun convertMillisToISO(millis: Long): String {
    val fixedInstant = Instant.ofEpochMilli(millis)
        .atOffset(ZoneOffset.UTC)
        .toLocalDate()
        .atTime(12, 0, 0)
        .atOffset(ZoneOffset.UTC)
        .toInstant()
    return fixedInstant.toString().substringBeforeLast("Z") + ".000Z"
}

fun convertISOToMillis(isoString: String): Long? {
    return try {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        val localDate = LocalDate.parse(isoString.substring(0, 10), formatter)
        localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerInputField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    minDateMillis: Long? = null,
    maxDateMillis: Long? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    val validator: (Long) -> Boolean = remember(minDateMillis, maxDateMillis) {
        { dateMillis ->
            val isAfterMin = if (minDateMillis != null) dateMillis >= minDateMillis else true
            val isBeforeMax = if (maxDateMillis != null) dateMillis <= maxDateMillis else true

            isAfterMin && isBeforeMax
        }
    }
    val selectableDates = remember(validator) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return validator(utcTimeMillis)
            }
            override fun isSelectableYear(year: Int): Boolean {
                return true
            }
        }
    }

    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = value.takeIf { it.isNotBlank() }?.let { convertISOToMillis(it) },
        selectableDates = selectableDates
    )

    val displayValue = value.takeIf { it.isNotBlank() }?.substring(0, 10) ?: ""

    Box(
        modifier = modifier
            .clickable { showDialog = true }
    ) {
        BonteInputField(
            modifier = Modifier.fillMaxWidth(),
            label = label,
            value = displayValue,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = label,
                    modifier = Modifier.clickable { showDialog = true }
                )
            }
        )
    }

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateState.selectedDateMillis?.let { millis ->
                            val isoString = convertMillisToISO(millis)
                            onValueChange(isoString)
                        }
                        showDialog = false
                    }
                ) { Text(stringResource(R.string.confirm)) }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        ) {
            DatePicker(
                state = dateState
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteAddExperienceDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onAdd: (ExperienceRequest) -> Unit,
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    val isFormValid = title.isNotBlank() && startDate.isNotBlank() && endDate.isNotBlank()
    val minDateMillis = remember(startDate) {
        startDate.takeIf { it.isNotBlank() }?.let { convertISOToMillis(it) }
    }
    val todayMillis = remember {
        LocalDate.now()
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimension.normal, vertical = AppTheme.dimension.normal)
        ) {
            Text(
                text = stringResource(R.string.addExperience),
                style = AppTheme.typography.large,
                modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                item {
                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.experience),
                        value = title,
                        onValueChange = { newText: String -> title = newText }
                    )

                    BonteInputField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.experienceDescription),
                        value = description,
                        onValueChange = { newText: String -> description = newText }
                    )

                    DatePickerInputField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.small),
                        label = stringResource(R.string.startDate),
                        value = startDate,
                        onValueChange = { startDate = it }
                    )

                    DatePickerInputField(
                        modifier = Modifier.fillMaxWidth().padding(bottom = AppTheme.dimension.normal),
                        label = stringResource(R.string.endDate),
                        value = endDate,
                        minDateMillis = minDateMillis,
                        maxDateMillis = todayMillis,
                        onValueChange = { endDate = it }
                    )
                }
            }

            BonteButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppTheme.dimension.normal),
                text = stringResource(R.string.confirm),
                enabled = isFormValid
            ) {
                val request = ExperienceRequest(
                    title = title,
                    description = description,
                    startDate = startDate,
                    endDate = endDate
                )
                onAdd(request)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BonteAddExperienceDialogPreview() {
    AppTheme {
        Box(modifier = Modifier.height(600.dp).background(AppTheme.color.background)) {
            BonteAddExperienceDialog(
                onDismiss = {},
                onAdd = { _ -> }
            )
        }
    }
}