package com.uwconnect.android.presentation.screens.clubs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.ClubBottomNavType
import com.uwconnect.android.domain.model.ClubEvent
import com.uwconnect.android.presentation.components.*
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.ClubViewModel
import com.uwconnect.android.presentation.viewmodel.EventViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.uwconnect.models.CreateEventRequest
import org.uwconnect.models.Event
import java.util.*

val colors = listOf(Red, Green, LightBlue, Yellow, BlueGrey, Teal, Cyan, Pink, Orange, White)

@Composable
fun CreateEventScreen(
    onNavigateToClubHomeScreen: () -> Unit,
    onNavigateToManageEventScreen: () -> Unit,
    onNavigateToClubProfileScreen: () -> Unit,
    eventViewModel: EventViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var eventTitle by remember { mutableStateOf(TextFieldValue("")) }
    var eventDescription by remember { mutableStateOf(TextFieldValue("")) }
    var eventLink by remember { mutableStateOf(TextFieldValue("")) }
    var eventLocation by remember { mutableStateOf(TextFieldValue("")) }
    val selectedColor = remember { mutableStateOf(Red) }

    // Start DateTime State
    val startYear = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val startMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val startDay = remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    val startHour = remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    val startMinute = remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }

    // End DateTime State
    val endYear = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val endMonth = remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val endDay = remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }
    val endHour = remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    val endMinute = remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    val startDateString = remember { mutableStateOf("") }
    val startTimeString = remember { mutableStateOf("") }
    val endDateString = remember { mutableStateOf("") }
    val endTimeString = remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf("") }
    val createEvent = {
        errorMessage = ""
        if (startDateString.value.isEmpty() || startTimeString.value.isEmpty() ||
            endDateString.value.isEmpty() || endTimeString.value.isEmpty()) {
            errorMessage = "Please select both start and end dates and times."
        } else {
            val newStartDateTime = LocalDateTime(startYear.value, startMonth.value + 1, startDay.value, startHour.value, startMinute.value)
            val newEndDateTime = LocalDateTime(endYear.value, endMonth.value + 1, endDay.value, endHour.value, endMinute.value)

            when {
                eventTitle.text.isBlank() -> errorMessage = "Event title cannot be empty"
                eventLocation.text.isBlank() -> errorMessage = "Event location cannot be empty"
                newStartDateTime > newEndDateTime || newEndDateTime == newStartDateTime -> errorMessage = "End time must be after start time"
                newEndDateTime < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) -> errorMessage = "End time must be set and occur in the future"
                newStartDateTime < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) -> errorMessage = "Start time must be set and occur in the future"
                else -> {
                    showConfirmationDialog = true
                }
            }
        }
    }
    Scaffold(
        bottomBar = {
            ClubNavBar(
                contentScreen = ClubBottomNavType.AddEvent,
                onNavigateToClubHomeScreen = { onNavigateToClubHomeScreen() },
                onNavigateToCreateEventScreen = { },
                onNavigateToManageEventScreen = { onNavigateToManageEventScreen() },
                onNavigateToClubProfileScreen = { onNavigateToClubProfileScreen() }
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
                    Text(
                        text = "Add Event",
                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
                        fontSize = 40.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
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
                        textColor = Color.White,
                        cursorColor = LightBlue,
                        onValueChanged = { newValue -> eventDescription = TextFieldValue(newValue) },
                        leadingIcon = Icons.Rounded.Description,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                item {
                    TextEntryModule(
                        description = "Event Link",
                        placeholder = "Enter event link",
                        leadingIcon = Icons.Default.Link,
                        textValue = eventLink.text,
                        textColor = Color.White,
                        cursorColor = LightBlue,
                        onValueChanged = { newValue -> eventLink = TextFieldValue(newValue) },
                        modifier = Modifier.fillMaxWidth(),
                        onTrailingIconClick = null,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextEntryModule(
                        description = "Event Location",
                        placeholder = "Enter event location",
                        leadingIcon = Icons.Default.LocationOn,
                        textValue = eventLocation.text,
                        textColor = Color.White,
                        cursorColor = LightBlue,
                        onValueChanged = { newValue -> eventLocation = TextFieldValue(newValue) },
                        modifier = Modifier.fillMaxWidth(),
                        onTrailingIconClick = null,
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(14.dp))
                }
                item {
                    DateTimePicker(
                        label = "Start",
                        year = startYear,
                        month = startMonth,
                        day = startDay,
                        hour = startHour,
                        minute = startMinute,
                        context = context,
                        dateString = startDateString,
                        timeString = startTimeString
                    )
                }
                item {
                    DateTimePicker(
                        label = "End",
                        year = endYear,
                        month = endMonth,
                        day = endDay,
                        hour = endHour,
                        minute = endMinute,
                        context = context,
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
                    Button(
                        onClick = { createEvent() },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .border(3.dp, LightBlue, RoundedCornerShape(6.dp)),
                        colors = buttonColors(DarkBlue),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            "Create Event",
                            modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                        )
                    }
                }
            }
            ConfirmationDialog(
                showDialog = showConfirmationDialog,
                title = "Confirm Creation",
                message = "Are you sure you want to create this event?",
                onConfirm = {
                    eventViewModel.createEvent(
                        CreateEventRequest(
                            eventTitle.text,
                            eventDescription.text,
                            eventLocation.text,
                            eventLink.text,
                            selectedColor.value.value.toString(),
                            LocalDateTime(
                                startYear.value,
                                startMonth.value + 1,
                                startDay.value,
                                startHour.value,
                                startMinute.value
                            ),
                            LocalDateTime(
                                endYear.value,
                                endMonth.value + 1,
                                endDay.value,
                                endHour.value,
                                endMinute.value
                            )
                        )
                    )
                    showConfirmationDialog = false
                    onNavigateToClubHomeScreen()
                },
                onCancel = {
                    showConfirmationDialog = false
                }
            )
        }
    }
}
