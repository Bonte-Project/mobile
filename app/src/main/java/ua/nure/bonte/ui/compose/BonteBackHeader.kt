package ua.nure.bonte.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column

@Composable
fun BonteBackHeader(
    modifier: Modifier = Modifier,
    text: String,
    showBackButton: Boolean = true,
    onBackClick: () -> Unit,
    onSettingsClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            style = AppTheme.typography.large.copy(
                color = AppTheme.color.foreground,
                textAlign = TextAlign.Center
            )
        )
        if (showBackButton) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.outline_arrow_back_24),
                        contentDescription = "Go Back",
                        tint = AppTheme.color.foreground
                    )
                }
            }
        }

        else if (onSettingsClick != null) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onSettingsClick) {
                    Icon(
                        painter = painterResource(R.drawable.settings_icon),
                        contentDescription = "Settings",
                        tint = AppTheme.color.foreground,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BonteBackHeaderPreview() {
    AppTheme {
        Column {
            BonteBackHeader(
                text = "Settings",
                onBackClick = {}
            )
            BonteBackHeader(
                text = "User Dashboard",
                showBackButton = false,
                onBackClick = {},
                onSettingsClick = {}
            )
        }
    }
}