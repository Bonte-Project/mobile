 package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteRecommendationCard(
    modifier: Modifier = Modifier,
    title: String,
    recommendationText: String,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .background(AppTheme.color.accent)
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
                    color = AppTheme.color.foreground.copy(alpha = 0.8f)
                ),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = AppTheme.dimension.normal)
            )
            Button(
                onClick = onClick,
                modifier = Modifier.padding(AppTheme.dimension.normal, vertical = AppTheme.dimension.small),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppTheme.color.background,
                    contentColor = AppTheme.color.foreground
                ),
                shape = RoundedCornerShape(AppTheme.dimension.small),
            ) {
                Text("View", style = AppTheme.typography.regular)
            }
        }
    }
}

@Preview
@Composable
fun RecommendationCardPreview() {
    AppTheme {
        BonteRecommendationCard(
            title = "Personal Recommendations",
            recommendationText = "Based on your activity, we suggest a new workout routine. Also, if your goal is to lose weight we suggest changing your breakfast habit. This is a very long recommendation text.",
            onClick = {}
        )
    }
}