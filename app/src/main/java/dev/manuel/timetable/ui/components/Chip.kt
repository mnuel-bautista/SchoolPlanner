package dev.manuel.timetable.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*

private val DayOfWeekChipHeight = 48.dp

@Composable
fun DayOfWeekChip(
    dayOfWeek: DayOfWeek,
    selected: Boolean = false,
    onClick: () -> Unit = {},
) {

    val backgroundAlpha by animateFloatAsState(
        if(selected) 0.12f else 0f
    )

    val backgroundColor = MaterialTheme.colors.primary.copy(
        alpha = backgroundAlpha
    )
    val selectedTextColor by animateColorAsState(
        if (selected) MaterialTheme.colors.primary else LocalContentColor.current
    )

    Surface(
        modifier = Modifier
            .clickable { onClick() },
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
        elevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(DayOfWeekChipHeight)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
                color = selectedTextColor,
                style = MaterialTheme.typography.body2,
            )

            if(selected) {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    tint = selectedTextColor,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    var selected by remember { mutableStateOf(false) }
    DayOfWeekChip(DayOfWeek.MONDAY, selected, onClick = { selected = !selected })
}