package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteInfoCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.color.accent, RoundedCornerShape(AppTheme.dimension.normal))
            .padding(AppTheme.dimension.normal)
    ) {
        Text(
            modifier = Modifier.padding(bottom = AppTheme.dimension.extraSmall),
            text = label,
            style = AppTheme.typography.large,
            color = AppTheme.color.foreground
        )
        Text(
            text = value,
            style = AppTheme.typography.regular,
            color = AppTheme.color.foreground
        )
    }
}

@Preview
@Composable
fun BonteInfoCardPreview() {
    AppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            BonteInfoCard(label = "Bio", value = "Professional fitness trainer with 5 years experience.")
            Spacer(modifier = Modifier.height(8.dp))
            BonteInfoCard(label = "Specialization", value = "Strength training, Yoga")
        }
    }
}
