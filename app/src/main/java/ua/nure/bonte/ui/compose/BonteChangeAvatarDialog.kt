package ua.nure.bonte.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ua.nure.bonte.R
import ua.nure.bonte.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteChangeAvatarDialog(
    modifier: Modifier = Modifier,
    avatar: String? = null,
    onAvatarChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        var avatarPreview by remember { mutableStateOf(avatar ?: "") }
        var avatarLink by remember { mutableStateOf(avatar ?: "") }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(130.dp)
                    .clip(shape = CircleShape)
                    .border(width = 1.dp, color = AppTheme.color.grey, shape = CircleShape),
                model = avatarPreview,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            BonteInputField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimension.normal,
                        vertical = AppTheme.dimension.normal
                    ),
                label = stringResource(R.string.avatar),
                value = avatarLink
            ) {
                avatarLink = it
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppTheme.dimension.normal,
                        vertical = AppTheme.dimension.normal
                    )
            ) {
                BonteButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(R.string.check)
                ) {
                    avatarPreview = avatarLink
                }
                Spacer(
                    modifier = Modifier.width(AppTheme.dimension.normal)
                )
                BonteButton(
                    modifier = Modifier.weight(1F),
                    text = stringResource(R.string.confirm)
                ) {
                    onAvatarChange(avatarLink)
                }
            }
        }
    }
}