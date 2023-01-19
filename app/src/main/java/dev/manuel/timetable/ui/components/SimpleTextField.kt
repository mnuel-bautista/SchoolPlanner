package dev.manuel.timetable.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle

@Composable
fun SimpleTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    style: TextStyle = androidx.compose.material3.LocalTextStyle.current,
    singleLine: Boolean = false,
    maxLines: Int = 1,
) {
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = style.copy(
            color = androidx.compose.material3.LocalContentColor.current.copy(
                alpha = ContentAlpha.high
            )
        ),
        cursorBrush = SolidColor(LocalContentColor.current),
        maxLines = maxLines,
        singleLine = singleLine,
    ) { innerTextField ->
        if (value.isBlank()) {
            androidx.compose.material3.Text(
                text = label,
                style = style.copy(
                    color = androidx.compose.material3.LocalContentColor.current.copy(
                        alpha = ContentAlpha.medium
                    )
                ),
            )
        }
        innerTextField()
    }
}