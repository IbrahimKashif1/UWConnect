package com.uwconnect.android.presentation.screens.members

//noinspection UsingMaterialAndMaterial3Libraries
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.uwconnect.android.presentation.components.SocialMediaLinksRow
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.ClubViewModel
import com.uwconnect.android.presentation.viewmodel.EventViewModel
import com.uwconnect.android.presentation.viewmodel.UserViewModel
import com.uwconnect.android.util.isUserClub
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.uwconnect.models.Event
import org.uwconnect.models.User
import com.uwconnect.android.presentation.components.FormatDateTime

@Composable
fun ViewClubScreen(
    navController: NavController,
    clubId: Int,
    onNavigateToViewEventScreen: (Int) -> Unit,
    clubViewModel: ClubViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    LaunchedEffect(clubId) {
        clubViewModel.fetchProfileById(clubId)
        userViewModel.fetchProfile()
    }

    val isLoading by clubViewModel.isLoading.collectAsState()
    val isEventViewModelLoading by eventViewModel.isLoading.collectAsState()
    val club by clubViewModel.queryProfile.collectAsState()
    val user by userViewModel.profile.collectAsState()

    var backPressed by remember { mutableStateOf(false) }

    val clubEvents = club?.events ?: emptyList()

    LaunchedEffect(isEventViewModelLoading) {
        userViewModel.fetchProfile()
        clubViewModel.fetchProfileById(clubId)
    }

    LaunchedEffect(isLoading) {
        userViewModel.fetchProfile()
    }

    val onEventButtonClickFactory = { eventId: Int ->
        { isUserParticipating: Boolean ->
            if (isUserParticipating) {
                eventViewModel.leaveEvent(eventId)
            } else {
                eventViewModel.joinEvent(eventId)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Club Details",
                        color = White,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                    )
                },
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
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            item { Spacer(modifier = Modifier.height(15.dp)) }
            item {
                Text(
                    text = "Club Name",
                    color = LightBlue,
                    fontSize = 28.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
            item {
                Text(
                    club?.name ?: "",
                    fontWeight = FontWeight.Normal,
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFamily,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(15.dp)) }
            if (club?.description?.isNotEmpty() == true) {
                item {
                    Text(
                        text = "Description",
                        color = LightBlue,
                        fontSize = 28.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                item {
                    Text(
                        club?.description ?: "",
                        fontWeight = FontWeight.Normal,
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                item { Spacer(modifier = Modifier.height(8.dp)) }
            }
            if (clubEvents.isNotEmpty()) {
                item {
                    Text(
                        text = "Events",
                        color = LightBlue,
                        fontSize = 28.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
                if (isLoading) {
                    items(5) {
                        // ShimmerAnimation()
                    }
                } else {
                    items(clubEvents) { event ->
                        val eventStarted =
                            event.start < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        if (user != null && !eventStarted) {
                            EventCard(
                                event,
                                user!!,
                                { onNavigateToViewEventScreen(event.id) },
                                onEventButtonClickFactory(event.id)
                            )
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(15.dp)) }
            }
            item {
                SocialMediaLinksRow(
                    instagramLink = club?.instagram,
                    discordLink = club?.discord,
                    facebookLink = club?.facebook
                )
            }
            item { Spacer(modifier = Modifier.height(5.dp)) }
            item {
                Button(
                    onClick = {
                        if (club != null) {
                            if (isUserClub(club!!.id, user)) {
                                clubViewModel.leave(club!!.id)
                            } else {
                                clubViewModel.join(club!!.id)
                            }
                        }
                        userViewModel.fetchProfile()
                        clubViewModel.fetchProfileById(clubId)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isUserClub(club?.id ?: 0, user)) DarkGreen else Gray
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isUserClub(club?.id ?: 0, user)) "Subscribed" else "Subscribe",
                        color = White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = PoppinsFamily
                    )
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Event, user: User, onCardClick: () -> Unit, onButtonClick: (Boolean) -> Unit) {
    val isUserParticipating = user.events?.any { it.id == event.id } == true

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onCardClick),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = if (isUserParticipating) {
            DarkGreen
        } else {
            Color(0xFF0A3E5D)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.title,
                        color = White,
                        fontSize = 20.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = event.location ?: "Location TBD",
                        color = White,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFamily,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (event.description.isNotEmpty()) {
                    Text(
                        text = event.description,
                        color = White,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFamily,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text(
                            text = "Event Start Time:     ${FormatDateTime(event.start.toString())}",
                            color = White,
                            fontSize = 12.sp,
                            fontFamily = PoppinsFamily,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Event End Time:     ${FormatDateTime(event.end.toString())}",
                            color = White,
                            fontSize = 12.sp,
                            fontFamily = PoppinsFamily,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = { onButtonClick(isUserParticipating) },
                        modifier = Modifier.padding(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = if (isUserParticipating) Gray else Green
                        )) {
                        Text(
                            text = if (isUserParticipating) "Leave" else "Join",
                            color = White,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}