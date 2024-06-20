package com.uwconnect.android.presentation.screens.clubs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uwconnect.android.presentation.components.*
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.EventViewModel
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import org.uwconnect.models.UpdateEventRequest

@OptIn(FormatStringsInDatetimeFormats::class)
@Composable
fun EditEventScreen(
    navController: NavController,
    eventId: Int,
    eventViewModel: EventViewModel = hiltViewModel(),
) {
    var backPressed by remember { mutableStateOf(false) }

    val isLoading by eventViewModel.isLoading.collectAsState()

    LaunchedEffect(eventId, isLoading) {
        eventViewModel.getEventById(eventId)
    }

    val event by eventViewModel.event.collectAsState()
    val context = LocalContext.current

    var eventTitle by remember { mutableStateOf(TextFieldValue("")) }
    var eventDescription by remember { mutableStateOf(TextFieldValue("")) }
    var eventLink by remember { mutableStateOf(TextFieldValue("")) }
    var eventLocation by remember { mutableStateOf(TextFieldValue("")) }

    val selectedColor = remember { mutableStateOf(Red) }

    val initialDateTime = remember { mutableStateOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())) }

    val startYear = remember { mutableIntStateOf(initialDateTime.value.year) }
    val startMonth = remember { mutableIntStateOf(initialDateTime.value.monthNumber) }
    val startDay = remember { mutableIntStateOf(initialDateTime.value.dayOfMonth) }
    val startHour = remember { mutableIntStateOf(initialDateTime.value.hour) }
    val startMinute = remember { mutableIntStateOf(initialDateTime.value.minute) }

    val endYear = remember { mutableIntStateOf(initialDateTime.value.year) }
    val endMonth = remember { mutableIntStateOf(initialDateTime.value.monthNumber) }
    val endDay = remember { mutableIntStateOf(initialDateTime.value.dayOfMonth) }
    val endHour = remember { mutableIntStateOf(initialDateTime.value.hour) }
    val endMinute = remember { mutableIntStateOf(initialDateTime.value.minute) }

    val dateFormat = LocalDateTime.Format {
        DateTimeFormat.formatAsKotlinBuilderDsl(LocalDate.Format {
            byUnicodePattern("yyyy-MM-dd")
        })
    }

    val timeFormat = LocalDateTime.Format {
        DateTimeFormat.formatAsKotlinBuilderDsl(LocalTime.Format {
            byUnicodePattern("HH:mm")
        })
    }

    val startDateString = remember {
        mutableStateOf(initialDateTime.value.format(dateFormat))
    }
    val startTimeString = remember {
        mutableStateOf(initialDateTime.value.format(timeFormat))
    }
    val endDateString = remember {
        mutableStateOf(initialDateTime.value.format(dateFormat))
    }
    val endTimeString = remember {
        mutableStateOf(initialDateTime.value.format(timeFormat))
    }

    LaunchedEffect(event) {
        if (event != null) {
            eventTitle = TextFieldValue(event!!.title)
            eventDescription = TextFieldValue(event!!.description)
            eventLink = TextFieldValue(event!!.link ?: "")
            eventLocation = TextFieldValue(event!!.location ?: "")
            selectedColor.value = Color(event!!.color.toULong())

            val startDateTime = event!!.start
            val endDateTime = event!!.end

            startYear.intValue = startDateTime.year
            startMonth.intValue = startDateTime.monthNumber - 1
            startDay.intValue = startDateTime.dayOfMonth
            startHour.intValue = startDateTime.hour
            startMinute.intValue = startDateTime.minute

            endYear.intValue = endDateTime.year
            endMonth.intValue = endDateTime.monthNumber - 1
            endDay.intValue = endDateTime.dayOfMonth
            endHour.intValue = endDateTime.hour
            endMinute.intValue = endDateTime.minute

            startDateString.value = startDateTime.format(dateFormat)
            startTimeString.value = startDateTime.format(timeFormat)
            endDateString.value = endDateTime.format(dateFormat)
            endTimeString.value = endDateTime.format(timeFormat)
        }
    }

    var errorMessage by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val updateEvent = {
        errorMessage = ""
        val newStartDateTime = LocalDateTime(startYear.intValue, startMonth.intValue + 1, startDay.intValue, startHour.intValue, startMinute.intValue)
        val newEndDateTime = LocalDateTime(endYear.intValue, endMonth.intValue + 1, endDay.intValue, endHour.intValue, endMinute.intValue)
        when {
            eventTitle.text.isBlank() -> errorMessage = "Event title cannot be empty"
            newStartDateTime > newEndDateTime || newEndDateTime == newStartDateTime -> errorMessage = "End time must be after start time"
            newEndDateTime < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) -> errorMessage = "End time must be set and occur in the future"
            newStartDateTime < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) -> errorMessage = "Start time must be set and occur in the future"
            else -> { showConfirmationDialog = true }
        }
    }
    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        title = "Confirm Update",
        message = "Are you sure you want to update this event?",
        onConfirm = {
            eventViewModel.updateEvent(
                eventId,
                UpdateEventRequest(
                    start = LocalDateTime(startYear.intValue, startMonth.intValue + 1, startDay.intValue, startHour.intValue, startMinute.intValue),
                    end = LocalDateTime(endYear.intValue, endMonth.intValue + 1, endDay.intValue, endHour.intValue, endMinute.intValue),
                    title = eventTitle.text,
                    location = eventLocation.text,
                    description = eventDescription.text,
                    link = eventLink.text,
                    color = selectedColor.value.value.toString()
                )
            )
            showConfirmationDialog = false
            if (!backPressed) {
                backPressed = true
                navController.popBackStack()
            }
        },
        onCancel = {
            showConfirmationDialog = false
        }
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Event", color = White, fontFamily = PoppinsFamily) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (!backPressed) {
                            backPressed = true
                            navController.popBackStack()
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                backgroundColor = DarkBlue,
                contentColor = White,
                elevation = 12.dp
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBlue)
        ) {
            LazyColumn(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    TextEntryModule(
                        description = "Event Title",
                        placeholder = "Enter event title",
                        leadingIcon = Icons.Default.Title,
                        textValue = eventTitle.text,
                        textColor = White,
                        cursorColor = LightBlue,
                        onValueChanged = { newValue -> eventTitle = TextFieldValue(newValue) },
                        modifier = Modifier.fillMaxWidth(),
                        onTrailingIconClick = null,
                    )
                }
                item { Spacer(modifier = Modifier.height(15.dp)) }
                item {
                    MultiLineTextEntryModule(
                        description = "Event Description",
                        placeholder = "Enter event description",
                        textValue = eventDescription.text,
                        textColor = White,
                        cursorColor = LightBlue,
                        onValueChanged = { newValue -> eventDescription = TextFieldValue(newValue) },
                        leadingIcon = Icons.Rounded.Description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    eventLink.let {
                        TextEntryModule(
                            description = "Event Link",
                            placeholder = "Enter event link",
                            leadingIcon = Icons.Default.Link,
                            textValue = it.text,
                            textColor = White,
                            cursorColor = LightBlue,
                            onValueChanged = { newValue -> eventLink = TextFieldValue(newValue) },
                            modifier = Modifier.fillMaxWidth(),
                            onTrailingIconClick = null,
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    eventLocation.let {
                        TextEntryModule(
                            description = "Event Location",
                            placeholder = "Enter event location",
                            leadingIcon = Icons.Default.LocationOn,
                            textValue = it.text,
                            textColor = White,
                            cursorColor = LightBlue,
                            onValueChanged = { newValue -> eventLocation = TextFieldValue(newValue) },
                            modifier = Modifier.fillMaxWidth(),
                            onTrailingIconClick = null,
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    DateTimePicker(
                        "Start",
                        startYear,
                        startMonth,
                        startDay,
                        startHour,
                        startMinute,
                        context,
                        dateString = startDateString,
                        timeString = startTimeString
                    )
                }
                item {
                    DateTimePicker(
                        "End",
                        endYear,
                        endMonth,
                        endDay,
                        endHour,
                        endMinute,
                        context,
                        dateString = endDateString,
                        timeString = endTimeString
                    )
                }
                item {
                    ColorPicker(colors = colors, selectedColor = selectedColor)
                }
                item {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { updateEvent() },
                            modifier = Modifier
                                .weight(1f)
                                .border(3.dp, LightBlue, RoundedCornerShape(6.dp)),
                            colors = buttonColors(backgroundColor = DarkBlue),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Update Event", modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                                color = White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (!backPressed) {
                                    backPressed = true
                                    navController.popBackStack()
                                } },
                            modifier = Modifier
                                .weight(1f)
                                .border(3.dp, Red, RoundedCornerShape(6.dp)),
                            colors = buttonColors(backgroundColor = DarkBlue),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Cancel", modifier = Modifier.padding(top = 5.dp, bottom = 5.dp),
                                color = White)
                        }
                    }
                }
            }
        }
    }
}