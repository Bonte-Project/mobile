package ua.nure.bonte.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteActivityCard(
    modifier: Modifier = Modifier,
    @DrawableRes iconRes: Int,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(AppTheme.dimension.normal))
            .padding(AppTheme.dimension.normal)
            .height(IntrinsicSize.Min)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier
                .size(AppTheme.dimension.iconSize)
                .padding(end = AppTheme.dimension.small),
            painter = painterResource(iconRes),
            contentDescription = title,
            tint = AppTheme.color.foreground
        )
        Text(
            modifier = Modifier.padding(start = AppTheme.dimension.extraSmall),
            text = title,
            style = AppTheme.typography.regular,
        )
        Spacer(
            modifier = Modifier
                .weight(1F)
                .fillMaxHeight()
        )
        Text(
            text = value,
            style = AppTheme.typography.regular.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview
@Composable
fun ActivityCardPreview() {
    AppTheme {
        Column {
            BonteActivityCard(
                iconRes = R.drawable.nutrition_icon,
                title = "Nutrition",
                value = "500 kcal",
                onClick = {}
            )
            BonteActivityCard(
                iconRes = R.drawable.yoga_icon,
                title = "Yoga",
                value = "60 min",
                onClick = {}
            )
        }
    }
}