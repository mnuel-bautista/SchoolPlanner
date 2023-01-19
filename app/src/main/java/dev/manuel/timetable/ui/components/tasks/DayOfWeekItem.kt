package dev.manuel.timetable.ui.components.tasks

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.manuel.timetable.format
import java.time.DayOfWeek

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayOfWeekItem(
    dayOfWeek: DayOfWeek,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
) {

    val modifier = if (onClick != null)
        Modifier.clickable { onClick() }
    else
        Modifier

    val backgroundAlpha by animateFloatAsState(
        if (selected) 0.12f else 0f
    )

    val backgroundColor = MaterialTheme.colorScheme.primary.copy(
        alpha = backgroundAlpha
    )
    val contentColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
    )

    Surface(
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        ListItem(
            modifier = Modifier.then(modifier)
                .height(56.dp),
            headlineText = { Text(text = dayOfWeek.format()) },
            trailingContent = {
                if (selected) {
                    Icon(imageVector = Icons.Outlined.Check, contentDescription = null)
                }
            }
        )
    }
}