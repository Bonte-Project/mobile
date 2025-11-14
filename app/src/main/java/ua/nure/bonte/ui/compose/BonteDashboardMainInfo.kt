package ua.nure.bonte.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteDashboardMainInfo(
    modifier: Modifier = Modifier,
    name: String,
    role: String,
    avatarUrl: String?,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimension.normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(
                model = avatarUrl ?: R.drawable.profile_placeholder,
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.profile_placeholder),
                error = painterResource(R.drawable.profile_placeholder),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(2.dp, AppTheme.color.active, CircleShape)
            )

            Column(
                modifier = Modifier.padding(start = AppTheme.dimension.normal)
            ) {
                Text(
                    text = name,
                    style = AppTheme.typography.large
                )
                Text(
                    text = role,
                    style = AppTheme.typography.regular
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardHeaderPreview() {
    AppTheme {
        BonteDashboardMainInfo(
            name = "John Smith",
            role = "premium user",
            avatarUrl = "https://placehold.co/128x128",
            onSettingsClick = {}
        )
    }
}
