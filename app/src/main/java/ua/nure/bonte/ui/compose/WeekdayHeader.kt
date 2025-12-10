package ua.nure.bonte.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.nure.bonte.ui.theme.AppTheme
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import kotlin.collections.forEach

@Composable
fun WeekdaysHeader(daysOfWeek: List<DayOfWeek>) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        daysOfWeek.forEach { daysOfWeek ->
            Text(
                modifier = Modifier
                    .padding(horizontal = 0.5.dp)
                    .background(color = AppTheme.color.grey.copy(alpha = .3F))
                    .padding(vertical = 4.dp)
                    .weight(1F),
                text = daysOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                style = AppTheme.typography.small.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}