package ua.nure.bonte.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@Composable
fun BonteDashboardHeader(
    modifier: Modifier = Modifier,
    name: String,
    role: String,
    onEditClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(AppTheme.dimension.normal),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.profile_placeholder),
                contentDescription = "User Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .border(2.dp, AppTheme.color.active, CircleShape)
            )

            Column(
                modifier = Modifier.padding(start = AppTheme.dimension.normal)
            ) {
                Text(
                    text = name,
                    style = AppTheme.typography.large.copy(
                        color = AppTheme.color.foreground
                    )
                )
                Text(
                    text = role,
                    style = AppTheme.typography.regular.copy(
                        color = AppTheme.color.grey
                    )
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onEditClick) {
                Icon(
                    painter = painterResource(R.drawable.edit_icon),
                    contentDescription = "Edit Profile",
                    tint = AppTheme.color.foreground,
                    modifier = Modifier.size(30.dp)
                )
            }

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

@Preview(showBackground = true)
@Composable
fun DashboardHeaderPreview() {
    AppTheme {
        BonteDashboardHeader(
            name = "John Smith",
            role = "premium user",
            onEditClick = {},
            onSettingsClick = {}
        )
    }
}