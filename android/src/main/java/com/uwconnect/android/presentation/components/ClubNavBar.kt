package com.uwconnect.android.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.uwconnect.android.domain.model.BottomNavType
import com.uwconnect.android.domain.model.ClubBottomNavType
import com.uwconnect.android.presentation.ui.*

@Composable
fun ClubNavBar(
    contentScreen: ClubBottomNavType,
    onNavigateToClubHomeScreen: () -> Unit,
    onNavigateToCreateEventScreen: () -> Unit,
    onNavigateToManageEventScreen: () -> Unit,
    onNavigateToClubProfileScreen: () -> Unit) {
    NavigationBar(containerColor = NightBlue,
        contentColor = MaterialTheme.colorScheme.onSurface) {
        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == ClubBottomNavType.Home) LightBlue else White
                ) },
            onClick = onNavigateToClubHomeScreen,
        )

        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add Event",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == ClubBottomNavType.AddEvent) LightBlue else White
                ) },
            onClick = onNavigateToCreateEventScreen
        )
        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.Edit,
                    contentDescription = "Edit Event",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == ClubBottomNavType.ManageEvent) LightBlue else White
                ) },
            onClick = onNavigateToManageEventScreen
        )

        NavigationBarItem(
            selected = false,
            icon = {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier.size(40.dp),
                    tint = if (contentScreen == ClubBottomNavType.Profile) LightBlue else White
                ) },
            onClick = onNavigateToClubProfileScreen
        )
    }
}

