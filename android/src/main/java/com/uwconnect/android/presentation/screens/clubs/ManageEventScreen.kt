package com.uwconnect.android.presentation.screens.clubs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RemoveRedEye
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
import androidx.navigation.NavController
import com.uwconnect.android.domain.model.ClubBottomNavType
import com.uwconnect.android.presentation.components.ClubNavBar
import com.uwconnect.android.presentation.components.ConfirmationDialog
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.ClubViewModel
import com.uwconnect.android.presentation.viewmodel.EventViewModel
import org.uwconnect.models.Event
import com.uwconnect.android.presentation.components.FormatDateTime

@Composable
fun ManageEventScreen(
    onNavigateToClubHomeScreen: () -> Unit,
    onNavigateToCreateEventScreen: () -> Unit,
    onNavigateToClubProfileScreen: () -> Unit,
    onEditEvent: (Int) -> Unit,
    onPreviewEvent: (Int) -> Unit,
    clubViewModel: ClubViewModel = hiltViewModel(),
    eventViewModel: EventViewModel = hiltViewModel()
) {
    val club by clubViewModel.profile.collectAsState()
    val isLoading by eventViewModel.isLoading.collectAsState()

    LaunchedEffect(isLoading) {
        Log.d("Launching effect", "launch")
        clubViewModel.fetchProfile()
    }

    Scaffold(
        bottomBar = {
            ClubNavBar(
                contentScreen = ClubBottomNavType.ManageEvent,
                onNavigateToClubHomeScreen = { onNavigateToClubHomeScreen() },
                onNavigateToCreateEventScreen = { onNavigateToCreateEventScreen() },
                onNavigateToManageEventScreen = { },
                onNavigateToClubProfileScreen = { onNavigateToClubProfileScreen() }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding).background(DarkBlue)
        ) {
            LazyColumn(
                modifier = Modifier.align(Alignment.TopCenter).padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                val eventsList = club?.events ?: emptyList()
                item {
                    Text(
                        text = "Manage Events",
                        modifier = Modifier.padding(top = 12.dp, bottom = 6.dp),
                        fontSize = 40.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
                items(eventsList) { event ->
                    EventItem(
                        event = event,
                        onDeleteEvent = { eventViewModel.deleteEvent(event.id) },
                        onEditEvent = { onEditEvent(event.id) },
                        onPreviewEvent = { onPreviewEvent(event.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EventItem(event: Event, onDeleteEvent: () -> Unit, onEditEvent: () -> Unit, onPreviewEvent: () -> Unit) {
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    ConfirmationDialog(
        showDialog = showDeleteConfirmationDialog,
        title = "Delete Event",
        message = "Are you sure you want to delete this event?",
        onConfirm = {
            onDeleteEvent()
            showDeleteConfirmationDialog = false
        },
        onCancel = {
            showDeleteConfirmationDialog = false
        }
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 25.dp)) {
            Text(text = event.title, fontWeight = FontWeight.Bold, color = White, fontSize = 20.sp, fontFamily = PoppinsFamily)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Event Begins: ${FormatDateTime(event.start.toString())} ${FormatDateTime(event.end.toString())}", color = OffWhite,
                fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis, fontFamily = PoppinsFamily
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = event.description, maxLines = 14, overflow = TextOverflow.Ellipsis, color = SkyBlue, fontSize = 11.sp, fontFamily = PoppinsFamily)
        }
        IconButton(onClick = { onPreviewEvent() }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.RemoveRedEye, contentDescription = "Preview", tint = BlueGrey)
        }
        Spacer(modifier = Modifier.width(25.dp))
        IconButton(onClick = { onEditEvent() }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = White)
        }
        Spacer(modifier = Modifier.width(25.dp))
        IconButton(onClick = { showDeleteConfirmationDialog = true }, modifier = Modifier.size(24.dp)) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = DarkRed)
        }
    }
    Divider(color = Gray, thickness = 1.dp)
}
