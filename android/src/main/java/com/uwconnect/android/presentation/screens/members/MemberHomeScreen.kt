package com.uwconnect.android.presentation.screens.members

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.BottomNavType
import com.uwconnect.android.presentation.components.MemberNavBar
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.UserViewModel
import kotlinx.datetime.*
import org.uwconnect.models.Announcement
import org.uwconnect.models.Event
import java.time.format.DateTimeFormatter

@Composable
fun MemberHomeScreen(
    onNavigateToCalendarScreen: () -> Unit,
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToViewEventScreen: (Int) -> Unit,
    userViewModel: UserViewModel = hiltViewModel(),
) {
    Scaffold(
        bottomBar = {
            MemberNavBar(
                contentScreen = BottomNavType.Home,
                onNavigateToHomeScreen = {},
                onNavigateToCalendarScreen = { onNavigateToCalendarScreen() },
                onNavigateToSearchScreen = { onNavigateToSearchScreen() },
                onNavigateToProfileScreen = { onNavigateToProfileScreen() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBlue)
        ) {
            item {
                Text(
                    text = "Homepage",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    fontSize = 40.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item { HomePageContent(onNavigateToViewEventScreen, userViewModel) }
            item { Spacer(modifier = Modifier.height(6.dp)) }
        }
    }
}

@Composable
fun ScrollableNotificationSection(
    events: List<Event>,
    onEventClick: (Int) -> Unit
) {
    Card(
        backgroundColor = NightBlue,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            items(events) { event ->
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                        .clickable(onClick = {onEventClick(event.id)})
                ) {
                    Text(
                        text = event.club.name,
                        color = White,
                        fontSize = 18.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = event.title,
                        color = SkyBlue,
                        fontSize = 13.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = event.start.toJavaLocalDateTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        color = BlueGrey,
                        fontSize = 11.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = "Location: " + event.location,
                        color = BlueGrey,
                        fontSize = 11.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Gray, thickness = 1.dp)
                }
            }
        }
    }
}


@Composable
fun ScrollableAnnouncementsSection(
    announcements: List<Announcement>,
) {
    Card(
        backgroundColor = NightBlue,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(8.dp)
        ) {
            items(announcements) { announcement ->
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = announcement.club.name,
                        color = White,
                        fontSize = 16.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = announcement.title,
                        color = SkyBlue,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = announcement.description,
                        color = White,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = announcement.timestamp.toJavaLocalDateTime()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                        color = BlueGrey,
                        fontSize = 11.sp,
                        fontFamily = PoppinsFamily,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = Gray, thickness = 1.dp)
                }
            }
        }
    }
}

@Composable
fun HomePageContent(
    onEventClick: (Int) -> Unit,
    userViewModel: UserViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        userViewModel.fetchProfile()
        userViewModel.getAnnouncements()
    }

    val user by userViewModel.profile.collectAsState()

    val upcomingEvents = user?.events?.filter {
        it.start > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }?.sortedBy {
        it.start.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    } ?: emptyList()

    val announcements by userViewModel.announcements.collectAsState()

    Text(
        text = "Upcoming Events",
        fontSize = 30.sp,
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        color = SkyBlue,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Card(
        backgroundColor = NightBlue,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .padding(top = 12.dp)
            .padding(horizontal = 20.dp)
            .height(350.dp)
    ) {
        if (upcomingEvents.isNotEmpty()) {
            ScrollableNotificationSection(upcomingEvents, onEventClick)
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "No upcoming events...",
                    color = White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    fontFamily = PoppinsFamily
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
    Text(
        text = "Announcements",
        fontSize = 30.sp,
        fontFamily = PoppinsFamily,
        fontWeight = FontWeight.SemiBold,
        color = SkyBlue,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Card(
        backgroundColor = NightBlue,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .padding(top = 12.dp)
            .padding(horizontal = 20.dp)
            .height(350.dp)
    ) {
        if (announcements.isNotEmpty()) {
            ScrollableAnnouncementsSection(announcements.sortedByDescending { it.timestamp.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds() })
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                androidx.compose.material3.Text(
                    text = "No announcements...",
                    color = White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    fontFamily = PoppinsFamily
                )
            }
        }
    }
}
