package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Alignment

@Composable
fun BonteHeader(
    modifier: Modifier = Modifier,
    text: String,
    type: BonteHeaderType = BonteHeaderType.Back,
    onCloseClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        when(type) {
            BonteHeaderType.Back -> {
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
            BonteHeaderType.Settings -> {
                Spacer(
                    modifier = Modifier
                        .size(42.dp)
                )
            }
            BonteHeaderType.Close -> {
                IconButton(
                    onClick = onCloseClick,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close),
                        contentDescription = "Go Back",
                        tint = AppTheme.color.foreground
                    )
                }
            }
        }

        Text(
            modifier = Modifier.weight(1F),
            text = text,
            style = AppTheme.typography.large.copy(
                textAlign = TextAlign.Center
            )
        )

        if(type == BonteHeaderType.Settings) {
            IconButton(
                onClick = onSettingsClick,
            ) {
                Icon(
                    painter = painterResource(R.drawable.settings_icon),
                    contentDescription = "Settings",
                    tint = AppTheme.color.foreground
                )
            }
        } else {
            Spacer(
                modifier = Modifier
                    .size(42.dp)
            )
        }
    }
}

enum class BonteHeaderType {
    Back, Settings, Close
}

@Preview(showBackground = true)
@Composable
fun BonteHeaderPreview() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.color.background)) {
            BonteHeader(
                text = "Settings",
                onCloseClick = {},
                onBackClick = {},
                onSettingsClick = {}
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BonteHeaderClosePreview() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.color.background)) {
            BonteHeader(
                text = "Settings",
                type = BonteHeaderType.Close,
                onCloseClick = {},
                onBackClick = {},
                onSettingsClick = {}
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
fun BonteHeaderSettingsPreview() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.color.background)) {
            BonteHeader(
                text = "Settings",
                type = BonteHeaderType.Settings,
                onCloseClick = {},
                onBackClick = {},
                onSettingsClick = {}
            )

        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BonteHeaderDarkPreview() {
    AppTheme {
        Box(modifier = Modifier.background(color = AppTheme.color.background)) {
            BonteHeader(
                text = "Settings",
                onCloseClick = {},
                onBackClick = {},
                onSettingsClick = {}
            )
        }
    }
}