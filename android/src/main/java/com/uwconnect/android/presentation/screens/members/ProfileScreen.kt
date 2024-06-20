package com.uwconnect.android.presentation.screens.members

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.firebase.messaging.FirebaseMessaging
import com.uwconnect.android.data.ApiClient
import com.uwconnect.android.domain.model.BottomNavType
import com.uwconnect.android.presentation.components.*
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.AuthViewModel
import com.uwconnect.android.presentation.viewmodel.LogoutViewModel
import com.uwconnect.android.presentation.viewmodel.UserViewModel
import com.uwconnect.android.util.NotificationManager
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.uwconnect.models.RegisterTokenRequest
import org.uwconnect.models.UpdateUserRequest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ProfileScreen(
    onNavigateToHomeScreen: (Int) -> Unit,
    onNavigateToCalendarScreen: (Int) -> Unit,
    onNavigateToSearchScreen: (Int) -> Unit,
    onLogoutSuccessNavigation: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
    userViewModel: UserViewModel = hiltViewModel(),
    logoutViewModel: LogoutViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) }
    val id by authViewModel.id.collectAsState()
    val memberProfile by userViewModel.profile.collectAsState()
    LaunchedEffect(key1 = Unit) { userViewModel.fetchProfile() }
    var isEditMode by remember { mutableStateOf(false) }
    var failMode by remember { mutableStateOf(false) }
    var emptyNameFail by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var editedName by remember(memberProfile) { mutableStateOf(memberProfile?.name ?: "") }
    var editedBio by remember(memberProfile) { mutableStateOf(memberProfile?.bio ?: "") }
    val isLoading by userViewModel.isLoading.collectAsState()
    val updateResult by userViewModel.updateResult.collectAsState()
    var originalName by remember { mutableStateOf("") }
    var originalBio by remember { mutableStateOf("") }
    val handleSubmissionAttempt = {
        if (editedName.isEmpty()) {
            emptyNameFail = true
            showConfirmationDialog = false
        } else {
            showConfirmationDialog = true
        }
    }
    val performUpdate = {
        userViewModel.clearUpdateResult()
        userViewModel.updateProfile(
            UpdateUserRequest(
                editedName,
                editedBio
            )
        )
    }
    LaunchedEffect(memberProfile) {
        memberProfile?.let {
            if (!isEditMode) {
                editedName = it.name
                editedBio = it.bio ?: ""
            }
        }
    }
    LaunchedEffect(Unit) {
        authViewModel.loadAuthState()
    }
    LaunchedEffect(updateResult) {
        updateResult?.let {
            it.onSuccess {
                showConfirmationDialog = false
                isEditMode = false
            }.onFailure {
                failMode = true
                showConfirmationDialog = false
            }
        }
    }
    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        title = "Confirm Creation",
        message = "Are you sure you want to update the profile?",
        onConfirm = {
            performUpdate() },
        onCancel = { showConfirmationDialog = false }
    )
    CustomAlertDialogue(
        showDialog = failMode,
        title = "Failed to Update",
        message = "Failed to update the profile. Please try again.",
        onDismissRequest = { failMode = false },
        singleButton = true,
        singleButtonText = "OK",
        singleButtonAction = { failMode = false }
    )
    CustomAlertDialogue(
        showDialog = emptyNameFail,
        title = "Failed to Update",
        message = "Requires non-empty name! Please try again.",
        onDismissRequest = { emptyNameFail = false },
        singleButton = true,
        singleButtonText = "OK",
        singleButtonAction = { emptyNameFail = false }
    )
    NavigationHelper(
        shouldNavigate = { logoutViewModel.logoutState.isLogoutSuccess },
        destination = { onLogoutSuccessNavigation() }
    )
    Scaffold(
        bottomBar = {
            MemberNavBar(
                contentScreen = BottomNavType.Profile,
                onNavigateToHomeScreen = { onNavigateToHomeScreen(id) },
                onNavigateToCalendarScreen = { onNavigateToCalendarScreen(id) },
                onNavigateToSearchScreen = { onNavigateToSearchScreen(id) },
                onNavigateToProfileScreen = { }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBlue)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = { logoutViewModel.onClearErrorMessage() })
                }
        ) {
            LazyColumn(
                modifier = Modifier.padding(bottom = 70.dp),
            ) {
                item {
                    Text(
                        text = "My Profile",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                            .padding(top = 0.dp, start = 10.dp, end = 10.dp, bottom = 0.dp),
                        fontSize = 40.sp,
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        color = White
                    )
                }
                if (isEditMode) {
                    item {
                        TextEntryModule(
                            description = "My Name",
                            placeholder = "Enter your name",
                            textValue = editedName,
                            textColor = White,
                            cursorColor = DarkBlue,
                            onValueChanged = { editedName = it },
                            trailingIcon = null,
                            onTrailingIconClick = null,
                            leadingIcon = Icons.Rounded.Person,
                            modifier = Modifier.padding(bottom = 10.dp, top = 0.dp, start = 10.dp, end = 10.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                    item {
                        MultiLineTextEntryModule(
                            description = "My Bio",
                            placeholder = "Enter a bio",
                            textValue = editedBio,
                            textColor = White,
                            cursorColor = DarkBlue,
                            onValueChanged = { editedBio = it },
                            leadingIcon = Icons.Rounded.Description,
                            modifier = Modifier.padding(horizontal = 10.dp),
                        )
                    }
                } else {
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                    item {
                        Text(
                            text = "My Name",
                            color = LightBlue,
                            fontSize = 28.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 20.dp, bottom = 4.dp)
                        )
                    }
                    item {
                        Text(
                            text = editedName,
                            color = White,
                            fontSize = 13.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 20.dp, bottom = 16.dp, end = 14.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(15.dp)) }
                    if (editedBio.isNotEmpty()) {
                        item {
                            Text(
                                text = "My Bio",
                                color = LightBlue,
                                fontSize = 28.sp,
                                fontFamily = PoppinsFamily,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 20.dp, bottom = 4.dp)
                            )
                        }
                        item {
                            Text(
                                text = editedBio,
                                color = White,
                                fontSize = 13.sp,
                                fontFamily = PoppinsFamily,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(start = 20.dp, bottom = 16.dp, end = 14.dp)
                            )
                        }
                        item { Spacer(modifier = Modifier.height(10.dp)) }
                    }
                    item {
                        Text(
                            text = "Notifications",
                            color = LightBlue,
                            fontSize = 28.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 20.dp, bottom = 4.dp)
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (hasNotificationPermission) "Enabled!" else "Disabled.",
                                color = White,
                                fontSize = 13.sp,
                                fontFamily = PoppinsFamily,
                                fontWeight = FontWeight.Normal
                            )
                            if (!hasNotificationPermission) {
                                Button(
                                    onClick = {
                                        NotificationManager.requestPermission()
                                        runBlocking {
                                            val token = FirebaseMessaging.getInstance().token.await()
                                            Log.d("firebase_token", token)
                                            ApiClient.apiService.registerToken(RegisterTokenRequest(token))
                                        }
                                        onNavigateToHomeScreen(id)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor = when (hasNotificationPermission) {
                                            true -> DarkGreen
                                            false -> LightBlue
                                        }
                                    ),
                                ) {
                                    Text("Enable Notifications", color = White, fontFamily = PoppinsFamily)
                                }
                            }
                        }
                    }
                    item{
                        Text(text = "If you've already tried to enable notifications, you must change in Settings!" ,
                            color = White,
                            fontSize = 13.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 20.dp, bottom = 16.dp, end = 14.dp)
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isEditMode) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AuthenticationButton(
                            text = "Submit",
                            backgroundColor = LightBlue,
                            contentColor = White,
                            enabled = !isLoading,
                            onButtonClick = {
                                handleSubmissionAttempt()
                            },
                            isLoading = isLoading,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        AuthenticationButton(
                            text = "Cancel",
                            backgroundColor = Gray,
                            contentColor = White,
                            enabled = true,
                            onButtonClick = {
                                editedName = originalName
                                editedBio = originalBio
                                isEditMode = false
                                failMode = false
                                emptyNameFail = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AuthenticationButton(
                            text = "Edit",
                            backgroundColor = LightBlue,
                            contentColor = White,
                            enabled = true,
                            onButtonClick = {
                                originalName = editedName
                                originalBio = editedBio
                                isEditMode = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp)
                        )
                        AuthenticationButton(
                            text = "Logout",
                            backgroundColor = DarkRed,
                            contentColor = White,
                            enabled = true,
                            onButtonClick = logoutViewModel::onLogoutClick,
                            isLoading = logoutViewModel.logoutState.isLoading,
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 0.dp)
                        )
                    }
                    Text(
                        text = logoutViewModel.logoutState.errorMessageLogout ?: "",
                        fontFamily = PoppinsFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 10.sp,
                        color = Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}