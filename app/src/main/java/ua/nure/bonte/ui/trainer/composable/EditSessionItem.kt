package ua.nure.bonte.ui.trainer.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.ui.theme.AppTheme
import java.time.format.DateTimeFormatter

@Composable
fun SessionEditItem(
    modifier: Modifier = Modifier,
    item: SessionEntity,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onStatusChange: (SessionEntity) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = AppTheme.shape.inputShape)
            .background(
                color = when (item.status) {
                    SessionStatus.scheduled -> AppTheme.color.active
                    SessionStatus.completed -> AppTheme.color.grey
                    SessionStatus.cancelled -> Color.Red
                }
            )
            .padding(horizontal = AppTheme.dimension.normal),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = AppTheme.dimension.normal),
            text = item.scheduledAt.format(DateTimeFormatter.ofPattern("HH:mm")),
            style = AppTheme.typography.regular
        )
        Text(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimension.normal),
            text = item.name,
            style = AppTheme.typography.regular,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(
            modifier = Modifier.weight(1F)
        )
        when (item.status) {
            SessionStatus.scheduled -> {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimension.small)
                        .clickable {
                            onStatusChange(
                                item.copy(
                                    status = SessionStatus.completed
                                )
                            )
                        },
                    painter = painterResource(R.drawable.completed),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            onStatusChange(
                                item.copy(
                                    status = SessionStatus.cancelled
                                )
                            )
                        },
                    painter = painterResource(R.drawable.cancel),
                    contentDescription = null
                )
            }

            SessionStatus.completed -> {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimension.small)
                        .clickable {
                            onStatusChange(
                                item.copy(
                                    status = SessionStatus.scheduled
                                )
                            )
                        },
                    painter = painterResource(R.drawable.schedule),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            onStatusChange(
                                item.copy(
                                    status = SessionStatus.completed
                                )
                            )
                        },
                    painter = painterResource(R.drawable.completed),
                    contentDescription = null
                )

            }

            SessionStatus.cancelled -> {
                Icon(
                    modifier = Modifier
                        .padding(horizontal = AppTheme.dimension.small)
                        .clickable {
                            onStatusChange(
                                item.copy(
                                    status = SessionStatus.scheduled
                                )
                            )
                        },
                    painter = painterResource(R.drawable.schedule),
                    contentDescription = null
                )
                Icon(
                    modifier = Modifier
                        .clickable {
                            onStatusChange(
                                item.copy(
                                    status = SessionStatus.completed
                                )
                            )
                        },
                    painter = painterResource(R.drawable.completed),
                    contentDescription = null
                )
            }
        }



        Icon(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimension.small)
                .clickable(onClick = onEdit),
            painter = painterResource(R.drawable.edit_icon),
            contentDescription = null
        )
        Icon(
            modifier = Modifier
                .clickable(onClick = onDelete),
            painter = painterResource(R.drawable.delete),
            contentDescription = null
        )

    }

}

@Preview(showBackground = true)
@Composable
private fun SessionEditItemPreview(modifier: Modifier = Modifier) {
    AppTheme {
        SessionEditItem(
            item = SessionEntity.preview.first(),
            onEdit = {},
            onDelete = {},
            onStatusChange = {}
        )
    }
}