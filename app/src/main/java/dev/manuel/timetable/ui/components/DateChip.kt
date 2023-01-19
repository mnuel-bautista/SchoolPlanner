package dev.manuel.timetable.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun DateChip(
    date: String,
    isActive: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {

    val color =
        if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.32f) else Color(0x30D8D6FF)

    Box(
        modifier = Modifier
            .clip(shape = RoundedCornerShape(size = 4.dp))
            .clickable(enabled = enabled) { onClick()  }
            .background(color)
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}