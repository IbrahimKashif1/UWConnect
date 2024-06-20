package com.uwconnect.android.presentation.screens.clubs

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Announcement
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.ClubBottomNavType
import com.uwconnect.android.domain.model.Notification
import com.uwconnect.android.presentation.components.*
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.ClubViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.uwconnect.models.Announcement
import org.uwconnect.models.CreateAnnouncementRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ClubHomeScreen(
    onNavigateToCreateEventScreen: () -> Unit,
    onNavigateToManageEventScreen: () -> Unit,
    onNavigateToClubHomeScreen: () -> Unit,
    onNavigateToClubProfileScreen: () -> Unit,
    clubViewModel: ClubViewModel = hiltViewModel()
) {
    val profile by clubViewModel.profile.collectAsState()
    LaunchedEffect(Unit) {
        clubViewModel.fetchProfile()
    }
    var announcementTitle by remember { mutableStateOf("") }
    var announcementDescription by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val upcomingEvents = (profile?.events?.filter { it.start > Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }) ?: emptyList()

    Scaffold(
        bottomBar = {
            ClubNavBar(
                contentScreen = ClubBottomNavType.Home,
                onNavigateToClubHomeScreen = { },
                onNavigateToCreateEventScreen = { onNavigateToCreateEventScreen() },
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
                modifier = Modifier.align(Alignment.TopCenter),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Text(
                        "Home Page",
                        fontSize = 40.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                item {
                    Text(
                        "Upcoming Event",
                        fontSize = 26.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = SkyBlue
                    )
                }
                item {
                    Card(
                        backgroundColor = NightBlue,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        if (!upcomingEvents.isEmpty()) {
                            Row(modifier = Modifier.padding(8.dp)) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = upcomingEvents[0].title,
                                        color = White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        fontFamily = PoppinsFamily
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = upcomingEvents[0].description,
                                        color = White,
                                        fontSize = 11.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        fontFamily = PoppinsFamily
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    upcomingEvents[0].location?.let {
                                        Text(
                                            text = it,
                                            color = BlueGrey,
                                            fontSize = 10.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontFamily = PoppinsFamily
                                        )
                                    }
                                }
                            }
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "No upcoming events...",
                                    color = White,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    fontFamily = PoppinsFamily
                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(
                        "Send Announcement",
                        fontSize = 26.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = SkyBlue
                    )
                }
                item {
                    TextEntryModule(
                        description = "Announcement Title",
                        placeholder = "Enter announcement title",
                        leadingIcon = Icons.AutoMirrored.Filled.Announcement,
                        textValue = announcementTitle,
                        textColor = White,
                        cursorColor = LightBlue,
                        onValueChanged = { announcementTitle = it },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp),
                        trailingIcon = null,
                        onTrailingIconClick = null
                    )
                }
                item { Spacer(modifier = Modifier.height(10.dp)) }
                item {
                    MultiLineTextEntryModule(
                        description = "Announcement Description",
                        placeholder = "Enter announcement description",
                        textValue = announcementDescription,
                        textColor = White,
                        cursorColor = LightBlue,
                        onValueChanged = { announcementDescription = it },
                        leadingIcon = Icons.Rounded.Description,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp)
                    )
                }
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { showConfirmationDialog = true },
                            modifier = Modifier.fillMaxWidth(0.5f)
                                .border(3.dp, LightBlue, RoundedCornerShape(6.dp)),
                            colors = ButtonDefaults.buttonColors(backgroundColor = DarkBlue),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Submit!", Modifier.padding(top = 5.dp, bottom = 5.dp),
                                color = White, fontFamily = PoppinsFamily
                            )
                        }
                    }
                }
                item {
                    ConfirmationDialog(
                        showDialog = showConfirmationDialog,
                        title = "Confirm Send",
                        message = "Are you sure you want to send this announcement?",
                        onConfirm = {
                            clubViewModel.createAnnouncement(CreateAnnouncementRequest(
                                announcementTitle,
                                announcementDescription,
                                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                            ))
                            showConfirmationDialog = false
                            onNavigateToClubHomeScreen()
                            showSuccessDialog = true
                        },
                        onCancel = {
                            showConfirmationDialog = false
                        }
                    )
                }
                item {
                    CustomAlertDialogue(
                        showDialog = showSuccessDialog,
                        title = "Announcement Sent",
                        message = "Your announcement has been sent successfully!",
                        onDismissRequest = { showSuccessDialog = false },
                        singleButton = true,
                        singleButtonText = "OK",
                        singleButtonAction = { showSuccessDialog = false }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(35.dp))
                    Text(
                        "Announcement History",
                        fontSize = 26.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = SkyBlue
                    )
                }
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(10.dp),
                        elevation = 4.dp,
                        backgroundColor = NightBlue,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.padding(8.dp)
                        ) {
                            items((profile?.announcements ?: emptyList()).reversed()) { announcement ->
                                AnnouncementItem(announcement)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement) {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a", Locale.US)
    val formattedTimestamp = announcement.timestamp.toJavaLocalDateTime().format(formatter)
    Column(modifier = Modifier.padding(6.dp)) {
        Text(
            text = announcement.title,
            color = White,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            fontFamily = PoppinsFamily
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = announcement.description,
            color = White.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontFamily = PoppinsFamily
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Posted: $formattedTimestamp",
            color = SkyBlue.copy(alpha = 0.8f),
            fontSize = 11.sp,
            fontFamily = PoppinsFamily
        )
        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = White.copy(alpha = 0.2f), thickness = 2.dp, modifier = Modifier.padding(vertical = 4.dp))
    }
}