package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteMetricCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(AppTheme.dimension.normal)
    ) {
        Column {
            Text(
                text = title,
                style = AppTheme.typography.small.copy(
                    color = AppTheme.color.foreground.copy(alpha = 0.7f),
                )
            )
            Text(
                text = value,
                style = AppTheme.typography.large.copy(
                    color = AppTheme.color.foreground,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}

@Preview
@Composable
fun BonteMetricCardPreview() {
    AppTheme {
        BonteMetricCard(
            title = "Calories",
            value = "2,150",
            // Використовуємо Active як приклад насиченого фону
            backgroundColor = AppTheme.color.active,
            onClick = {}
        )
    }
}