package ua.nure.bonte.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun BonteVerticalColumnGraph(
    modifier: Modifier = Modifier,
    values: List<Triple<Int, String, LocalDateTime>>
) {
    val maxValue = values.maxOfOrNull { it.first }.takeIf { it != null && it > 0 } ?: 1

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        values.forEach { (value, label, date) ->
            ColumnItem(
                value = value,
                label = label,
                maxValue = maxValue,
                isSelected = date.toLocalDate() == LocalDate.now()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BonteVerticalColumnGraphPreview(modifier: Modifier = Modifier) {
    AppTheme() {
        BonteVerticalColumnGraph(
            values = listOf(
                Triple(0, "mon", LocalDateTime.now()),
                Triple(4, "tue", LocalDateTime.now().minusDays(1)),
                Triple(5, "wed", LocalDateTime.now().minusDays(3)),
                Triple(6, "thu", LocalDateTime.now()),
                Triple(10, "fri", LocalDateTime.now()),

            )
        )
    }
}