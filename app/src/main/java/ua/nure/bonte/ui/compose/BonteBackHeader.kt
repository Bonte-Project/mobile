package ua.nure.bonte.ui.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton // <-- Використовуємо IconButton для клікабельної іконки
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import androidx.annotation.DrawableRes

@Composable
fun BonteBackHeader(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int = R.drawable.outline_arrow_back_24,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = "Go Back",
                tint = AppTheme.color.foreground
            )
        }
        Text(
            modifier = Modifier.padding(start = 50.dp),
            text = text,
            style = AppTheme.typography.large.copy(
                color = AppTheme.color.foreground
            )
        )

    }
}
@Preview(showBackground = true)
@Composable
fun BonteBackHeaderNewPreview() {
    AppTheme {
        BonteBackHeader(
            text = "Create account",
            onBackClick = {},
            icon = R.drawable.outline_arrow_back_24
        )
    }
}