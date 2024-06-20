package com.uwconnect.android.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uwconnect.android.domain.model.BottomNavType
import com.uwconnect.android.presentation.ui.*

@Composable
fun MemberNavBar(
    contentScreen: BottomNavType,
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToCalendarScreen: () -> Unit,
    onNavigateToSearchScreen: () -> Unit,
    onNavigateToProfileScreen: () -> Unit) {
    NavigationBar(containerColor = NightBlue,
        contentColor = MaterialTheme.colorScheme.onSurface) {
        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == BottomNavType.Home) LightBlue else White
                ) },
            onClick = onNavigateToHomeScreen,
        )

        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.CalendarMonth,
                    contentDescription = "Calendar",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == BottomNavType.Calendar) LightBlue else White
                ) },
            onClick = onNavigateToCalendarScreen
        )
        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == BottomNavType.Search) LightBlue else White
                ) },
            onClick = onNavigateToSearchScreen
        )

        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == BottomNavType.Profile) LightBlue else White
                ) },
            onClick = onNavigateToProfileScreen
        )
    }
}

