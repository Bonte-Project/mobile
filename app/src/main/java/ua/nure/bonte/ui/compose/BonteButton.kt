package ua.nure.bonte.ui.compose

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteButton(
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    @DrawableRes icon: Int? = null,
    text: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = AppTheme.shape.buttonShape,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = AppTheme.color.active
        )
    ) {
        icon?.let {
            Image(
                modifier = Modifier
                    .padding(end = AppTheme.dimension.normal)
                    .size(24.dp),
                painter = painterResource(R.drawable.google),
                contentDescription = null
            )
        }
        Text(
            modifier = textModifier,
            text = text,
            style = AppTheme.typography.regular.copy(
                color = AppTheme.color.background
            )
        )

    }
}

@Preview(showBackground = true)
@Composable
fun BonteButtonPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            BonteButton(
                text = "Log in"
            ) {

            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun BonteButtonDarkPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = modifier.background(color = AppTheme.color.background)) {
            BonteButton(
                icon = R.drawable.google,
                text = "Log in"
            ) {

            }
        }
    }
}