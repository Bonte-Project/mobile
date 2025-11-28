package ua.nure.bonte.ui.compose

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.util.TableInfo
import io.ktor.websocket.Frame
import ua.nure.bonte.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ColumnItem(
    modifier: Modifier = Modifier,
    value: Int,
    label: String,
    isSelected: Boolean = false,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
        ) {
        Spacer(
            modifier = Modifier
                .width(24.dp)
                .height(if(value == 0) 5.dp else (value * 24).dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(if(isSelected) AppTheme.color.active else AppTheme.color.accent)
        )
        Text(
            text = label,
            style = AppTheme.typography.small
        )
    }
}

@Preview()
@Composable
private fun ColumnItemPreview(modifier: Modifier = Modifier) {
    AppTheme{
        Row() {
            ColumnItem(
                modifier = modifier,
                value = 0,
                label = "Mon",
                isSelected = true
            )
            ColumnItem(
                modifier = modifier,
                value = 10,
                label = "Mon"
            )
        }

    }
}