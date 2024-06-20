package com.uwconnect.android.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.uwconnect.android.presentation.screens.LoginScreen
import com.uwconnect.android.presentation.screens.SignupScreen
import com.uwconnect.android.presentation.screens.clubs.*
import com.uwconnect.android.presentation.screens.members.*

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Navigation(loginType: String) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = when(loginType) {
            "MEMBER" -> {
                ScreenRoutes.HomeScreen.route
            }
            "CLUB" -> {
                ScreenRoutes.ClubHomeScreen.route
            }
            else -> {
                ScreenRoutes.LoginScreen.route
            }
        }
    ){
        composable(ScreenRoutes.LoginScreen.route) {
            LoginScreen(
                onLoginSuccessNavigation = { type ->
                    when (type) {
                        "MEMBER" -> {
                            navController.navigate(ScreenRoutes.HomeScreen.createRoute()) {
                                popUpTo(0)
                            }
                        }
                        "CLUB" -> {
                            navController.navigate(ScreenRoutes.ClubHomeScreen.createRoute()) {
                                popUpTo(0)
                            }
                        }
                    }
                },
                onNavigateToSignupScreen = {
                    navController.navigate(ScreenRoutes.SignupScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(ScreenRoutes.SignupScreen.route) {
            SignupScreen(
                onNavigateToLoginScreen = {
                    navController.navigate(ScreenRoutes.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.HomeScreen.route,
        ) {
            MemberHomeScreen(
                onNavigateToCalendarScreen = {
                    navController.navigate(ScreenRoutes.CalendarScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToSearchScreen = {
                    navController.navigate(ScreenRoutes.SearchScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToProfileScreen = {
                    navController.navigate(ScreenRoutes.ProfileScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToViewEventScreen = { id ->
                    navController.navigate(ScreenRoutes.ViewEventScreen.createRoute(id))
                }
            )
        }
        composable(
            route = ScreenRoutes.CalendarScreen.route,
        ) {
            CalendarScreen(
                onNavigateToHomeScreen = {
                    navController.navigate(ScreenRoutes.HomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToSearchScreen = {
                    navController.navigate(ScreenRoutes.SearchScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToProfileScreen = {
                    navController.navigate(ScreenRoutes.ProfileScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToViewEventScreen = { event ->
                    navController.navigate(ScreenRoutes.ViewEventScreen.createRoute(event)) {
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.SearchScreen.route,
        ) {
            SearchScreen(
                onNavigateToHomeScreen = {
                    navController.navigate(ScreenRoutes.HomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToCalendarScreen = {
                    navController.navigate(ScreenRoutes.CalendarScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToProfileScreen = {
                    navController.navigate(ScreenRoutes.ProfileScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToClubDetails = { clubId ->
                    navController.navigate(ScreenRoutes.ViewClubScreen.createRoute(clubId)) {
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.ProfileScreen.route,
        ) {
            ProfileScreen(
                onNavigateToHomeScreen = {
                    navController.navigate(ScreenRoutes.HomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToCalendarScreen = {
                    navController.navigate(ScreenRoutes.CalendarScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToSearchScreen = {
                    navController.navigate(ScreenRoutes.SearchScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onLogoutSuccessNavigation = {
                    navController.navigate(ScreenRoutes.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(
            route = ScreenRoutes.ClubHomeScreen.route,
        ) {_ ->
            ClubHomeScreen(
                onNavigateToCreateEventScreen = {
                    navController.navigate(ScreenRoutes.CreateEventScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToManageEventScreen = {
                    navController.navigate(ScreenRoutes.ManageEventScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToClubHomeScreen = {
                    navController.navigate(ScreenRoutes.ClubHomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToClubProfileScreen = {
                    navController.navigate(ScreenRoutes.ClubProfileScreen.createRoute()) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.ClubProfileScreen.route,
        ) { _ ->
            ClubProfileScreen(
                onNavigateToClubHomeScreen = {
                    navController.navigate(ScreenRoutes.ClubHomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToCreateEventScreen = {
                    navController.navigate(ScreenRoutes.CreateEventScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToManageEventScreen = {
                    navController.navigate(ScreenRoutes.ManageEventScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onLogoutSuccessNavigation = {
                    navController.navigate(ScreenRoutes.LoginScreen.route) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.CreateEventScreen.route,
        ) { _ ->
            CreateEventScreen(
                onNavigateToClubHomeScreen = {
                    navController.navigate(ScreenRoutes.ClubHomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToClubProfileScreen = {
                    navController.navigate(ScreenRoutes.ClubProfileScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToManageEventScreen = {
                    navController.navigate(ScreenRoutes.ManageEventScreen.createRoute()) {
                        popUpTo(0)
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.ManageEventScreen.route,
        ) { _ ->
            ManageEventScreen(
                onNavigateToClubHomeScreen = {
                    navController.navigate(ScreenRoutes.ClubHomeScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToCreateEventScreen = {
                    navController.navigate(ScreenRoutes.CreateEventScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onNavigateToClubProfileScreen = {
                    navController.navigate(ScreenRoutes.ClubProfileScreen.createRoute()) {
                        popUpTo(0)
                    }
                },
                onEditEvent = { eventId ->
                    navController.navigate(ScreenRoutes.EditEventScreen.createRoute(eventId)) {
                    }
                },
                onPreviewEvent = { eventId ->
                    navController.navigate(ScreenRoutes.PreviewEventScreen.createRoute(eventId)) {
                    }
                }
            )
        }
        composable(
            route = ScreenRoutes.EditEventScreen.route,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            EditEventScreen(navController, eventId = eventId)
        }

        composable(
            route = ScreenRoutes.PreviewEventScreen.route,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            PreviewEventScreen(navController, eventId = eventId)
        }
        composable(
            route = ScreenRoutes.ViewClubScreen.route,
            arguments = listOf(
                navArgument("clubId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val clubId = backStackEntry.arguments?.getInt("clubId") ?: return@composable
            ViewClubScreen(navController, clubId, onNavigateToViewEventScreen = {
                eventId -> navController.navigate(ScreenRoutes.ViewEventScreen.createRoute(eventId))
            })
        }
        composable(
            route = ScreenRoutes.ViewEventScreen.route,
            arguments = listOf(navArgument("eventId") { type = NavType.IntType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: return@composable
            ViewEventScreen(navController, eventId = eventId)
        }
    }
}

sealed class ScreenRoutes(val route: String) {
    data object LoginScreen : ScreenRoutes("login")
    data object SignupScreen : ScreenRoutes("signup")
    data object HomeScreen : ScreenRoutes("home_screen") {
        fun createRoute() = "home_screen"
    }
    data object CalendarScreen : ScreenRoutes("calendar_screen") {
        fun createRoute() = "calendar_screen"
    }
    data object SearchScreen : ScreenRoutes("search_screen") {
        fun createRoute() = "search_screen"
    }
    data object ProfileScreen : ScreenRoutes("profile_screen") {
        fun createRoute() = "profile_screen"
    }
    object ViewClubScreen : ScreenRoutes("view_club_screen/{clubId}") {
        fun createRoute(clubId: Int) = "view_club_screen/$clubId"
    }
    data object ViewEventScreen : ScreenRoutes("view_event_screen/{eventId}") {
        fun createRoute(eventId: Int) = "view_event_screen/$eventId"
    }


    data object ClubHomeScreen : ScreenRoutes("club_home_screen") {
        fun createRoute() = "club_home_screen"
    }
    data object ClubProfileScreen : ScreenRoutes("club_profile_screen") {
        fun createRoute() = "club_profile_screen"
    }
    data object CreateEventScreen : ScreenRoutes("create_event_screen") {
        fun createRoute() = "create_event_screen"
    }
    data object ManageEventScreen : ScreenRoutes("manage_event_screen") {
        fun createRoute() = "manage_event_screen"
    }
    data object EditEventScreen : ScreenRoutes("edit_event_screen/{eventId}") {
        fun createRoute(eventId: Int) = "edit_event_screen/$eventId"
    }
    data object PreviewEventScreen : ScreenRoutes("preview_event_screen/{eventId}") {
        fun createRoute(eventId: Int) = "preview_event_screen/$eventId"
    }
}