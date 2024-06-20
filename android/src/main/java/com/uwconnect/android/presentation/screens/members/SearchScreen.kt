package com.uwconnect.android.presentation.screens.members

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.BottomNavType
import com.uwconnect.android.domain.model.UserClub
import com.uwconnect.android.presentation.components.MemberNavBar
import com.uwconnect.android.presentation.components.TextEntryModule
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.ClubViewModel
import com.uwconnect.android.presentation.viewmodel.UserViewModel
import com.uwconnect.android.util.isUserClub
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import org.uwconnect.models.Club
import org.uwconnect.models.User

@Composable
fun SearchScreen(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToCalendarScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit,
    onNavigateToClubDetails: (Int) -> Unit,
    clubViewModel: ClubViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel()
) {
    var searchText by remember { mutableStateOf("") }

    val isLoading by clubViewModel.isLoading.collectAsState()
    val clubs by clubViewModel.clubs.collectAsState()

    val profile by userViewModel.profile.collectAsState()

    val filteredClubs = clubs.filter {
        searchText.isBlank()
                || it.name.lowercase().contains(searchText.lowercase())
                || it.description?.lowercase()?.contains(searchText.lowercase()) == true
    }

    LaunchedEffect(Unit) {
        clubViewModel.fetchAll()
        userViewModel.fetchProfile()
    }

    LaunchedEffect(isLoading) {
        userViewModel.fetchProfile()
    }

    Scaffold(
        bottomBar = {
            MemberNavBar(
                contentScreen = BottomNavType.Search,
                onNavigateToHomeScreen = { onNavigateToHomeScreen() },
                onNavigateToCalendarScreen = { onNavigateToCalendarScreen() },
                onNavigateToSearchScreen = {},
                onNavigateToProfileScreen = { onNavigateToProfileScreen() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBlue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Search Clubs",
                    textAlign = TextAlign.Right,
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 0.dp),
                    fontSize = 40.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    color = White
                )
                TextEntryModule(
                    description = "",
                    placeholder = "Enter Club Name",
                    textValue = searchText,
                    textColor = White,
                    cursorColor = LightBlue,
                    onValueChanged = { searchText = it },
                    trailingIcon = null,
                    onTrailingIconClick = null,
                    leadingIcon = Icons.Rounded.Search,
                    modifier = Modifier.padding(bottom = 10.dp, top = 0.dp, start = 10.dp, end = 10.dp)
                )
            }
            if (isLoading && searchText.isNotEmpty()) {
                items(5) {
                    ShimmerAnimation()
                }
            } else {
                items(filteredClubs) { club ->
                    ClubRow(
                        club = club,
                        onToggleSubscribe = {
                            if (isUserClub(club.id, profile)) {
                                clubViewModel.leave(club.id)
                            } else {
                                clubViewModel.join(club.id)
                            }
                        },
                        onCardClick = {
                            onNavigateToClubDetails(club.id)
                        },
                        profile
                    )
                }
            }
        }
    }
}

@Composable
fun ClubRow(
    club: Club,
    onToggleSubscribe: (Club) -> Unit,
    onCardClick: (Club) -> Unit,
    profile: User?,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick(club) },
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color(0xFF0A3E5D)
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
                Text(
                    text = club.name,
                    color = White,
                    fontSize = 20.sp,
                    fontFamily = PoppinsFamily,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = club.description ?: "",
                    color = White,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFamily,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Button(
                onClick = { onToggleSubscribe(club) },
                modifier = Modifier.width(110.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (isUserClub(club.id, profile)) DarkGreen else Gray
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Text(
                    text = if (isUserClub(club.id, profile)) {
                        "Subscribed"
                    } else {
                        "Subscribe"
                    },
                    color = White,
                    fontFamily = PoppinsFamily,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
fun ShimmerAnimation() {
    val shimmerColors = listOf(
        DarkBlue.copy(alpha = 0.6f),
        DarkBlue.copy(alpha = 1f),
        DarkBlue.copy(alpha = 0.7f)
    )
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val xShimmer = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "Loading..."
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(xShimmer.value - 1000f, 0f),
        end = Offset(xShimmer.value, 0f)
    )

    ShimmerItem(brush = brush)
}

@Composable
fun ShimmerItem(brush: Brush) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        backgroundColor = NightBlue,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .height(60.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(brush, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(20.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(24.dp)
                        .background(brush, shape = RoundedCornerShape(10.dp))
                )
            }
        }
    }
}
