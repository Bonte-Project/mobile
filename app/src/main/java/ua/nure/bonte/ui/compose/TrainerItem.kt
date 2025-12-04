package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun TrainerItem(
    modifier: Modifier = Modifier,
    avatarUrl: String?,
    fullName: String?,
    specialization: String,
    onClick: () -> Unit,
    onLoadProfile: () -> Unit
    ) {

    LaunchedEffect(key1 = Unit) {
        if(fullName == null) {
            onLoadProfile()
        }
    }

    Row(modifier = modifier
        .fillMaxWidth()
        .clickable {
            onClick()
        },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .padding(all = AppTheme.dimension.normal)
                .size(90.dp)
                .clip(shape = CircleShape)
                .border(width = 1.dp, color = AppTheme.color.foreground, shape = CircleShape),
            model = avatarUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column() {
            Text(
                text = fullName ?: "",
                style = AppTheme.typography.large
            )
            Text(
                text = specialization,
                style = AppTheme.typography.regular
            )
        }

    }
}

@Preview
@Composable
fun TrainerItemPreview(modifier: Modifier = Modifier) {
    AppTheme {
        Box(modifier = Modifier.background(AppTheme.color.background)) {
            TrainerItem(
                avatarUrl = "",
                fullName = "John Dow",
                specialization = "boxer",
                onClick = {},
                onLoadProfile = {}
            )
        }
    }
}