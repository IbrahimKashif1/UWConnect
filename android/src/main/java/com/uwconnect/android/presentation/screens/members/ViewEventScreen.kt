package com.uwconnect.android.presentation.screens.members

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uwconnect.android.presentation.viewmodel.EventViewModel
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.UserViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import com.uwconnect.android.presentation.components.FormatDateTime

@Composable
fun ViewEventScreen(
    navController: NavController,
    eventId: Int,
    eventViewModel : EventViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    val isLoading by eventViewModel.isLoading.collectAsState()

    LaunchedEffect(eventId) {
        eventViewModel.getEventById(eventId)
    }

    LaunchedEffect(isLoading) {
        userViewModel.fetchProfile()
    }

    val user by userViewModel.profile.collectAsState()
    val event by eventViewModel.event.collectAsState()

    val isUserParticipating = user?.events?.any { it.id == eventId } == true

    var backPressed by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details", color = White, fontFamily = PoppinsFamily) },
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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(DarkBlue)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            item {
                Heading(text = "Club Name")
                Text(
                    text = event?.club?.name ?: "",
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFamily,
                       modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                Heading(text = "Event Name")
                Text(
                    text = "${event?.title}",
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            if (event?.description?.isNotEmpty() == true) {
                item {

                    Heading(text = "Event Description")
                    Text(
                        text = "${event?.description}",
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            if (event?.location?.isNotEmpty() == true) {
                item {
                    Heading(text = "Event Location")
                    Text(
                        text = "${event?.location}",
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            item {
                Heading(text = "Event Start Time")
                val dateTime = event?.start ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    text = FormatDateTime(dateTime.toString()),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item {
                Heading(text = "Event End Time")
                val dateTime = event?.end ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                Text(
                    text = FormatDateTime(dateTime.toString()),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            if (event?.link?.isNotEmpty() == true) {
                item {
                    val context = LocalContext.current
                    Heading(text = "Event Link")
                    ClickableText(
                        text = AnnotatedString(event?.link!!),
                        onClick = { _ ->
                            openLink(context, event?.link!!)
                        },
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = PoppinsFamily
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        if (isUserParticipating) {
                            eventViewModel.leaveEvent(eventId)
                        } else {
                            eventViewModel.joinEvent(eventId)
                        }
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    colors = if (isUserParticipating) {
                        buttonColors(backgroundColor = Gray)
                    } else {
                        buttonColors(backgroundColor = DarkGreen)
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isUserParticipating) {
                            "Leave"
                        } else {
                            "Join"
                        },
                        color = White
                    )
                }
            }
        }
    }
}

@Composable
fun Heading(text: String) {
    Text(
        text = text,
        color = LightBlue,
        fontSize = 28.sp,
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

fun openLink(context: Context, url: String) {
    val validUrl = addHTTPS(url)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(validUrl))
    context.startActivity(intent)
}

fun addHTTPS(url: String): String {
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
    }
}
