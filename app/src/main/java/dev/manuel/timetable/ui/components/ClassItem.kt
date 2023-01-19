package dev.manuel.timetable.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.Class as MClass

@Composable
fun ClassItem(
    mClass: MClass,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    shape: Shape = RoundedCornerShape(size = 4.dp),
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 0.dp,
        color = color,
        shape = shape,
    ) {
        Row {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "mClass.starts.format()",
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "mClass.ends.format()",
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 16.dp,
                    ),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = mClass.subject,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Icon(
                        imageVector = Icons.Outlined.Person,
                        tint = LocalContentColor.current.copy(
                            alpha = ContentAlpha.medium
                        ),
                        contentDescription = null
                    )

                    Text(
                        text = mClass.teacher,
                        style = MaterialTheme.typography.caption,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.classroom_icon),
                        tint = LocalContentColor.current.copy(
                            alpha = ContentAlpha.medium
                        ),
                        contentDescription = null
                    )
                    Text(
                        text = mClass.classroom,
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
    }
}
