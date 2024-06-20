package com.uwconnect.android.presentation.screens.members

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.BottomNavType
import com.uwconnect.android.presentation.components.MemberNavBar
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.UserViewModel
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.uwconnect.models.Event
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

private val pageBackgroundColor = DarkBlue
private val itemBackgroundColor = DarkBlue.copy(alpha = 0.8f)
private val selectedItemColor = SkyBlue
private val activeTextColor = White
private val inactiveTextColor = Gray

@Composable
fun CalendarScreen(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToViewEventScreen: (Int) -> Unit,
    userViewModel : UserViewModel = hiltViewModel()
) {
    val user by userViewModel.profile.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchProfile()
    }

    androidx.compose.material3.Scaffold(
        bottomBar = {
            MemberNavBar(
                contentScreen = BottomNavType.Calendar,
                onNavigateToHomeScreen = { onNavigateToHomeScreen() },
                onNavigateToCalendarScreen = {},
                onNavigateToSearchScreen = { onNavigateToSearchScreen() },
                onNavigateToProfileScreen = { onNavigateToProfileScreen() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBlue),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Your Events",
                textAlign = TextAlign.Right,
                modifier = Modifier.padding(10.dp),
                fontSize = 40.sp,
                fontFamily = PoppinsFamily,
                fontWeight = FontWeight.SemiBold,
                color = White
            )
            UserClubCalendar(user?.events ?: emptyList(), onNavigateToViewEventScreen)
        }
    }
}

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) java.time.format.TextStyle.SHORT else java.time.format.TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false): String {
    return getDisplayName(java.time.format.TextStyle.SHORT, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

@Composable
private fun CalendarNavigationIcon(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
) = Box(
    modifier = Modifier
        .fillMaxHeight()
        .aspectRatio(1f)
        .clip(shape = CircleShape)
        .clickable(role = Role.Button, onClick = onClick),
) {
    Icon(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .align(Alignment.Center),
        imageVector = icon, contentDescription = contentDescription, tint = White
    )
}

@Composable
fun SimpleCalendarTitle( modifier: Modifier, currentMonth: YearMonth,
                         goToPrevious: () -> Unit, goToNext: () -> Unit,
) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CalendarNavigationIcon(
            icon = Icons.AutoMirrored.Rounded.ArrowBackIos,
            contentDescription = "Previous",
            onClick = goToPrevious,
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .testTag("MonthTitle"),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            fontFamily = PoppinsFamily,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            color = White
        )
        CalendarNavigationIcon(
            icon = Icons.AutoMirrored.Rounded.ArrowForwardIos,
            contentDescription = "Next",
            onClick = goToNext,
        )
    }
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeLast()
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeFirst()
            }
            visibleItemsInfo.map { it.month }
        }
    }

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull().collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .border(
                width = if (isSelected) 1.dp else 0.dp,
                color = if (isSelected) selectedItemColor else NightBlue,
            )
            .padding(1.dp)
            .background(color = itemBackgroundColor)
            .clickable(
                enabled = day.position == DayPosition.MonthDate, onClick = { onClick(day) },
            ),
    ) {
        val textColor = when (day.position) {
            DayPosition.MonthDate -> activeTextColor
            DayPosition.InDate -> activeTextColor
            DayPosition.OutDate -> inactiveTextColor
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 12.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            for (color in colors) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .background(color),)
            }
        }
    }
}

@Composable
private fun MonthHeader(
    modifier: Modifier = Modifier,
    daysOfWeek: List<DayOfWeek> = emptyList(),
) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontFamily = PoppinsFamily,
                color = Color.White,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light,
            )
        }
    }
}

val clubDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE'\n'dd MMM'\n'HH:mm")

@Composable
fun UserClubCalendar(events: List<Event>, onNavigateToViewEventScreen: (Int) -> Unit) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }
    val state = rememberCalendarState(
        startMonth = startMonth, endMonth = endMonth,
        firstVisibleMonth = currentMonth, firstDayOfWeek = daysOfWeek.first(),
        outDateStyle = OutDateStyle.EndOfGrid,
    )
    val coroutineScope = rememberCoroutineScope()
    val visibleMonth = rememberFirstCompletelyVisibleMonth(state)

    LaunchedEffect(visibleMonth) {  selection = null }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackgroundColor),
    ) {
        item {
            SimpleCalendarTitle(
                modifier = Modifier
                    .background(EveningBlue)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                currentMonth = visibleMonth.yearMonth,
                goToPrevious = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
                },
            )
            HorizontalCalendar(
                modifier = Modifier.wrapContentWidth(),
                state = state,
                dayContent = { day ->
                    println("Day position: ${day.position}, month date: ${DayPosition.MonthDate}")
                    val colors = if (day.position == DayPosition.MonthDate) {

                        events.filter { event -> event.start.dayOfMonth <= day.date.dayOfMonth
                                && event.end.dayOfMonth >= day.date.dayOfMonth
                                && Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) <= event.end
                                && event.start.month <= day.date.month
                                && event.end.month >= day.date.month
                                && event.start.year <= day.date.year
                                && event.end.year >= day.date.year
                        }.map { event ->
                            Color(event.color.toULong())
                        }
                    } else {
                        emptyList()
                    }
                    Day(
                        day = day,
                        isSelected = selection == day,
                        colors = colors,
                    ) { clicked ->
                        selection = clicked
                    }
                },
                monthHeader = {
                    MonthHeader(
                        modifier = Modifier.padding(vertical = 8.dp),
                        daysOfWeek = daysOfWeek,
                    )
                },
            )
            Divider(color = pageBackgroundColor)
        }
        selection?.date?.let { date ->
            items(events.filter{
                        it.start.year <= date.year
                        && it.end.year >= date.year
                        && it.start.monthNumber <= date.monthValue
                        && it.end.monthNumber >= date.monthValue
                        && it.start.dayOfMonth <= date.dayOfMonth
                        && it.end.dayOfMonth >= date.dayOfMonth
                        && Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) <= it.end
            }) {event ->
                ClubEventInformation(event) { onNavigateToViewEventScreen(event.id) }
            }
        }
    }
}

@Composable
private fun ClubEventInformation(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(4.dp)
            .background(NightBlue),
        shape = RoundedCornerShape(10.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .background(color = Color(event.color.toULong()))
                .fillMaxWidth()
                .width(70.dp)
                .padding(start = 10.dp),
        ) {
            Column(modifier = Modifier.padding(top = 12.dp, end = 9.dp)) {
                val padMinuteStart =
                    if (event.start.minute.toInt() < 10) "0${event.start.minute}" else "${event.start.minute}"
                val padMinuteEnd =
                    if (event.end.minute.toInt() < 10) "0${event.start.minute}" else "${event.end.minute}"

                val textStyle = TextStyle(
                    textAlign = TextAlign.Center,
                    color = Black,
                    lineHeight = 17.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        text = "${event.start.month} ${event.start.dayOfMonth}, ${event.start.year}, ",
                        style = textStyle
                    )
                    Text(
                        text = "${event.start.hour}:$padMinuteStart",
                        style = textStyle
                    )
                }

                Text(text = "to", style = textStyle, modifier = Modifier.align(Alignment.CenterHorizontally))

                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(
                        text = "${event.start.month} ${event.start.dayOfMonth}, ${event.start.year}, ",
                        style = textStyle
                    )
                    Text(
                        text = "${event.end.hour}:$padMinuteEnd",
                        style = textStyle
                    )
                }
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(NightBlue)
                    .padding(8.dp)
            ) {
                Text(
                    text = event.club.name + " - " + event.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = event.location ?: "Location TBD",
                    maxLines = 1,
                    color = BlueGrey,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = event.description,
                    maxLines = 2,
                    color = White,
                    fontFamily = PoppinsFamily,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp
                )
            }
        }
    }
}