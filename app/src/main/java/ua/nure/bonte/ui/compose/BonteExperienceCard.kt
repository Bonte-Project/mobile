package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.repository.dto.ExperienceRequest
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteExperienceCard(
    exp: ExperienceRequest,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.color.accent, RoundedCornerShape(AppTheme.dimension.normal))
            .padding(AppTheme.dimension.normal)
    ) {
        Text(
            text = exp.title ?: "Untitled",
            style = AppTheme.typography.large,
            color = AppTheme.color.foreground
        )
        if (!exp.description.isNullOrBlank()) {
            Text(
                modifier = Modifier.padding(vertical = AppTheme.dimension.extraSmall),
                text = exp.description,
                style = AppTheme.typography.regular,
                color = AppTheme.color.foreground
            )
        }
        if (!exp.startDate.isNullOrBlank() || !exp.endDate.isNullOrBlank()) {
            Text(
                text = "From: ${exp.startDate ?: "-"}  To: ${exp.endDate ?: "-"}",
                style = AppTheme.typography.regular,
                color = AppTheme.color.foreground
            )
        }
    }
}

@Preview
@Composable
fun ExperienceCardPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BonteExperienceCard(
                exp = ExperienceRequest(
                    title = "Personal Trainer",
                    description = "Trained clients in strength and conditioning",
                    startDate = "2020-01-01",
                    endDate = "2022-06-30"
                )
            )
        }
    }
}
