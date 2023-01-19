package dev.manuel.timetable.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.*
import dev.manuel.timetable.R
import dev.manuel.timetable.format
import dev.manuel.timetable.ui.screens.classes.ClassState
import dev.manuel.timetable.ui.theme.TimetableTheme
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.*


@OptIn(ExperimentalPagerApi::class)
@Composable
fun TimetableScreen(
    viewModel: TimetableVM,
    pagerState: PagerState,
    onCourseClick: (courseId: Long) -> Unit,
    onCreateCourse: () -> Unit,
) {

    val courses by viewModel.courses.collectAsState()

    ScreenContent(
        courses = emptyList(),
        pagerState = pagerState,
        onCreateCourse = onCreateCourse,
        onCourseClick = onCourseClick
    )
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ScreenContent(
    courses: List<ClassState>,
    pagerState: PagerState,
    onCourseClick: (courseId: Long) -> Unit,
    onCreateCourse: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateCourse,
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null)
            }
        },
    ) { padding ->
        TimetableContent(
            modifier = Modifier.padding(padding),
            courses = courses,
            pagerState = pagerState,
            onCourseClick = onCourseClick,
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun TimetableContent(
    modifier: Modifier = Modifier,
    courses: List<ClassState>,
    onCourseClick: (courseId: Long) -> Unit,
    pagerState: PagerState,
) {
    HorizontalPager(modifier = modifier, count = 5, state = pagerState) { page ->
        LazyColumn(
            Modifier
                .fillMaxSize(),
        ) {
            items(courses.filter { it.dayOfWeek == enumValues<DayOfWeek>()[page] }) { course ->
                ClassItem(
                    mClass = course,/*
                    modifier = Modifier.padding(horizontal = 4.dp),*/
                    onClick = { onCourseClick(course.id) }
                )
                Divider()
            }
        }
    }
}

/**
 * Enum used for describing the top destinations of the application.
 * */
enum class NavigationItem {
    Subjects, Tasks, Teachers, Timetables
}

@Composable
private fun MoreOptions(
    onNavigate: (NavigationItem) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        DropdownMenu(expanded = expanded, onDismissRequest = {
            expanded = false
        }) {

            DropdownMenuItem(onClick = {
                expanded = false; onNavigate(NavigationItem.Timetables)
            }) {
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(id = R.string.timetables_item))
            }

            DropdownMenuItem(onClick = { expanded = false; onNavigate(NavigationItem.Tasks) }) {
                Icon(imageVector = Icons.Outlined.Checklist, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(id = R.string.task_list))
            }

            DropdownMenuItem(onClick = { expanded = false; onNavigate(NavigationItem.Subjects) }) {
                Icon(imageVector = Icons.Outlined.Class, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(id = R.string.subjects))
            }

            DropdownMenuItem(onClick = { expanded = false; onNavigate(NavigationItem.Teachers) }) {
                Icon(imageVector = Icons.Outlined.Person, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(id = R.string.teachers))
            }
        }


        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ClassItem(
    mClass: ClassState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 0.dp,
        shape = RoundedCornerShape(size = 4.dp),
    ) {
        Row {

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = mClass.startTime.format(),
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = mClass.startTime.format(),
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
                    text = mClass.subject ?: "",
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
                        text = mClass.professor ?: "",
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
                        text = mClass.classroom ?: "",
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ClassItemPreview() {
    ClassItem(
        mClass = courses.first(),
        onClick = {}
    )
}


@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
private fun DefaultPreview() {
    TimetableTheme {
        ScreenContent(
            courses,
            pagerState = rememberPagerState(),
            onCreateCourse = {},
            onCourseClick = {},
        )
    }
}


//Sample data for UI preview
private val courses = (0..10).map {
    ClassState(
        id = 0,
        subject = "Taller de base de datos $it",
        classroom = "LE5",
        professor = "Juan Carlos Madrigal Perez $it",
        dayOfWeek = DayOfWeek.MONDAY,
    )
}