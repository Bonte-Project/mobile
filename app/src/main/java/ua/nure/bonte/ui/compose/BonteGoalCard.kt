package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteGoalCard(
    modifier: Modifier = Modifier,
    title: String,
    recommendationText: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .background(AppTheme.color.accent)
            .clickable(onClick = onClick)
            .padding(AppTheme.dimension.normal)
    ) {
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = AppTheme.typography.regular.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = AppTheme.dimension.small)
            )
            Text(
                text = recommendationText,
                style = AppTheme.typography.small.copy(
                    color = AppTheme.color.foreground
                )
            )
        }
    }
}

@Preview
@Composable
fun RecommendationCardPreview() {
    AppTheme {
        BonteGoalCard(
            title = "Today's Goal",
            recommendationText = "Calories: 1500/3000"
        )
    }
}
