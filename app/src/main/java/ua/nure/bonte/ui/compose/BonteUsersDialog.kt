package ua.nure.bonte.ui.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.ProfileEntity
import ua.nure.bonte.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BonteSelectUsersDialog(
    modifier: Modifier = Modifier,
    items: List<UserHolder>? = null,
    onDismiss: () -> Unit,
    onUserSelect: (ProfileEntity) -> Unit,
    onLoadUser: (String) -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = AppTheme.color.background,
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimension.small)
        ) {
            items(items = items ?: emptyList(), ) { (userId, profile) ->

                LaunchedEffect(key1 = userId, key2 = profile) {
                    snapshotFlow {
                        profile == null
                    }.distinctUntilChanged()
                        .filter { it }
                        .collect {
                            onLoadUser(userId)
                        }
                }

                Row(modifier = modifier
                    .padding(horizontal = AppTheme.dimension.normal)
                    .fillMaxWidth()
                    .clip(shape = AppTheme.shape.inputShape)
                    .border(width = 1.dp, color = AppTheme.color.grey, shape = AppTheme.shape.accentShape)
                    .clickable {
                        profile?.let {
                            onUserSelect(it)
                        }
                    },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    profile?.let {
                        AsyncImage(
                            modifier = Modifier
                                .padding(all = AppTheme.dimension.normal)
                                .size(90.dp)
                                .clip(shape = CircleShape)
                                .border(width = 1.dp, color = AppTheme.color.foreground, shape = CircleShape),
                            model = it.avatarUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } ?: run {
                        Spacer(
                            modifier = Modifier
                                .padding(all = AppTheme.dimension.normal)
                                .size(90.dp)
                        )
                    }
                    Text(
                        text = profile?.fullName ?: "",
                        style = AppTheme.typography.large
                    )

                }
            }
        }

    }
    
}

data class UserHolder(
    val userId: String,
    val user: ProfileEntity? = null,
)