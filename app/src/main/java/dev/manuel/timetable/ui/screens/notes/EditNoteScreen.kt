package dev.manuel.timetable.ui.screens.notes

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.manuel.timetable.R
import dev.manuel.timetable.room.entities.Note

enum class BottomBarState {
    COLOR, DEFAULT
}


@Composable
fun EditNoteScreen(
    viewModel: EditNoteVM,
    onSelectClass: () -> Unit,
    navController: NavController,
) {

    val noteWithClass by viewModel.noteWithClass.collectAsState()

    EditNoteScreenContent(
        note = noteWithClass.note,
        mClass = noteWithClass.mClass,
        onTitleChange = viewModel::changeTitle,
        onContentChange = viewModel::changeContent,
        onSelectClass = onSelectClass,
        onSaveChanges = viewModel::saveChanges
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditNoteScreenContent(
    note: Note,
    mClass: dev.manuel.timetable.room.entities.Class?,
    onTitleChange: (String) -> Unit = {},
    onContentChange: (String) -> Unit = {},
    onSelectClass: () -> Unit,
    onSaveChanges: () -> Unit,
) {

    Log.d("EditNoteScreen", LocalContentColor.current.toString())
    Scaffold(
        topBar = { Box(Modifier.size(0.dp)) },
        contentColor = LocalContentColor.current,
        bottomBar = {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSaveChanges) {
                Text(stringResource(id = R.string.save))
            }
        }
    ) { padding ->
        Surface(modifier = Modifier.padding(padding), contentColor = LocalContentColor.current) {
            Column {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {

                    TextButton(onClick = onSelectClass) {
                        Text(text = mClass?.subject ?: "Sin asignatura")
                    }

                    TextField(
                        modifier = Modifier
                            .paddingFrom(FirstBaseline, before = 40.dp, after = 0.dp)
                            .semantics { contentDescription = "Note Title" },
                        value = note.title,
                        placeholder = "Note title",
                        onValueChange = onTitleChange,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    TextField(
                        modifier = Modifier
                            .paddingFromBaseline(top = 24.dp)
                            .semantics { contentDescription = "Note Content" },
                        value = note.content,
                        placeholder = "Note content",
                        onValueChange = onContentChange,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Divider()
            }
        }
    }
}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "",
    singleLine: Boolean = false,
    style: TextStyle = LocalTextStyle.current,
) {
    Box {
        if (value.isEmpty()) {
            Text(
                modifier = modifier,
                text = placeholder,
                style = style,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
            )
        }
        BasicTextField(
            modifier = modifier,
            value = value,
            cursorBrush = SolidColor(LocalContentColor.current),
            singleLine = singleLine,
            onValueChange = onValueChange,
            textStyle = style.copy(color = LocalContentColor.current),
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BottomAppBar(
    bottomBarState: BottomBarState = BottomBarState.DEFAULT,
    onSelectCollection: (Int) -> Unit = {},
    onShareClicked: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    onCopyClicked: () -> Unit = {},
    onColorPaletteClicked: () -> Unit = {},
    colorPicker: @Composable () -> Unit,
) {

    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalContentAlpha provides ContentAlpha.medium,
        ) {
            AnimatedContent(modifier = Modifier.weight(1f), targetState = bottomBarState) {
                when (it) {
                    BottomBarState.COLOR -> {
                        colorPicker()
                    }
                    BottomBarState.DEFAULT -> {
                        Row {
                            IconButton(onClick = onColorPaletteClicked) {
                                Icon(
                                    imageVector = Icons.Outlined.Palette,
                                    contentDescription = "Change Note Color"
                                )
                            }
                            IconButton(onClick = onCopyClicked) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = "Copy Note"
                                )
                            }
                            IconButton(onClick = onShareClicked) {
                                Icon(
                                    imageVector = Icons.Outlined.Share,
                                    contentDescription = "Share note"
                                )
                            }
                            IconButton(onClick = onDeleteClicked) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = "Delete note"
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .width(136.dp)
                .fillMaxHeight()
        ) {
            //Position a box above the content, so that clicks are not passed to the Icon Button and Text.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(),
                        onClick = {}
                    )
            )
            Row(
                Modifier
                    .width(136.dp)
                    .fillMaxHeight()
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = rememberRipple(),
                        onClick = { }
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    imageVector = Icons.Outlined.ListAlt,
                    contentDescription = null,
                )

                Text(
                    text = "Materiales",
                    style = MaterialTheme.typography.titleSmall,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        }
    }
}
