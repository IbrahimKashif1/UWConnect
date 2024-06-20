package com.uwconnect.android.presentation.screens.clubs

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.uwconnect.android.presentation.ui.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.presentation.viewmodel.EventViewModel
import com.uwconnect.android.presentation.components.FormatDateTime
import android.content.Context
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Icon
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle

@Composable
fun PreviewEventScreen(
    navController: NavController,
    eventId: Int,
    eventViewModel: EventViewModel = hiltViewModel()
) {
    LaunchedEffect(eventId) {
        eventViewModel.getEventById(eventId)
    }
    val event by eventViewModel.event.collectAsState()
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = DarkBlue,
                contentColor = Color.White,
                elevation = 12.dp
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .background(DarkBlue)
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Heading(text = "Event Name")
                    Text(
                        text = event?.title ?: "",
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    if (event?.description?.isNotEmpty() == true) {
                        Heading(text = "Event Description")
                        Text(
                            text = event?.description ?: "",
                            fontWeight = FontWeight.Normal,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontFamily = PoppinsFamily,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(15.dp))
                    }
                    Heading(text = "Event Location")
                    Text(
                        text = event?.location ?: "",
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    Heading(text = "Event Start Time")
                    Text(
                        text = FormatDateTime(event?.start.toString()),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    Heading(text = "Event End Time")
                    Text(
                        text = FormatDateTime(event?.end.toString()),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(15.dp))

                    if (event?.link?.isNotEmpty() == true) {
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
                        Spacer(modifier = Modifier.height(8.dp))
                    }
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
    val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(validUrl))
    context.startActivity(intent)
}

fun addHTTPS(url: String): String {
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "https://$url"
    } else {
        url
    }
}
