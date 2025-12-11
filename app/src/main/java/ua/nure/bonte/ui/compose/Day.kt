package ua.nure.bonte.ui.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipAnchorPosition
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.core.CalendarDay
import ua.nure.bonte.R
import ua.nure.bonte.db.data.entity.SessionEntity
import ua.nure.bonte.repository.dto.SessionStatus
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val TAG = "Day"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Day(
    day: CalendarDay,
    sessions: List<SessionEntity>? = null,
    isEditEnabled: Boolean = false,
    onDayClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    TooltipBox(
        tooltip = {
            if (sessions?.isNotEmpty() == true) {
                Column(
                    modifier = Modifier
                        .width(200.dp),
                ) {
                    sessions.take(15).sortedBy { it.scheduledAt }.forEach {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clip(shape = RoundedCornerShape(4.dp))
                                .background(
                                    color = when (it.status) {
                                        SessionStatus.scheduled -> AppTheme.color.active
                                        SessionStatus.completed -> AppTheme.color.grey
                                        SessionStatus.cancelled -> Color.Red
                                    },
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = AppTheme.dimension.small)
                            ,
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = AppTheme.dimension.small),
                                text = it.scheduledAt.format(DateTimeFormatter.ofPattern("HH:mm")),
                                style = AppTheme.typography.regular
                            )
                            Text(
                                modifier = Modifier
                                    .padding(start = AppTheme.dimension.normal)
                                    .weight(1F),
                                text = it.name,
                                style = AppTheme.typography.regular
                            )
                        }
                    }

                }
            }
        },
        state = rememberTooltipState(),
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            positioning = TooltipAnchorPosition.Above,
            spacingBetweenTooltipAndAnchor = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.5F)
                .padding(1.dp)
                .clip(shape = RoundedCornerShape(4.dp))
                .border(
                    width = 0.5.dp,
                    shape = RoundedCornerShape(4.dp),
                    color = AppTheme.color.grey.copy(
                        alpha = 0.2F
                    )
                )
                .clickable(onClick = onDayClick)
        ) {
            if(sessions?.isNotEmpty() == true && isEditEnabled) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = AppTheme.shape.accentShape)
                        .background(color = Color.White)
                        .border(width = 1.dp, color = AppTheme.color.active, shape = AppTheme.shape.accentShape)
                        .padding(4.dp)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                        ,
                        text = day.date.dayOfMonth.toString(),
                        style = AppTheme.typography.small
                    )

                    Icon(
                        modifier = Modifier
                            .padding(start = AppTheme.dimension.small)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(color = Color.White, shape = CircleShape)
                            .border(width = 1.dp, color = AppTheme.color.active, shape = CircleShape)
                            .padding(2.dp)
                            .clickable(onClick = onEditClick)
                        ,
                        painter = painterResource(R.drawable.edit_icon),
                        contentDescription = null,
                    )
                }

            } else {
                Text(
                    modifier = Modifier
                    ,
                    text = day.date.dayOfMonth.toString(),
                    style = AppTheme.typography.small
                )
            }

            Column(
                modifier = Modifier
                    .weight(1F),
                verticalArrangement = Arrangement.Bottom
            ) {
                sessions?.forEach {
                    Box(
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .height(8.dp)
                            .fillMaxWidth()
                            .background(
                                color = when (it.status) {
                                    SessionStatus.scheduled -> AppTheme.color.active
                                    SessionStatus.completed -> AppTheme.color.grey
                                    SessionStatus.cancelled -> Color.Red
                                }
                            )
                    )

                }

            }
        }

    }


}