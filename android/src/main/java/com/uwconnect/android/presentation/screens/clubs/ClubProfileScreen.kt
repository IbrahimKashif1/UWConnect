package com.uwconnect.android.presentation.screens.clubs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ConnectWithoutContact
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uwconnect.android.domain.model.ClubBottomNavType
import com.uwconnect.android.presentation.components.*
import com.uwconnect.android.presentation.ui.*
import com.uwconnect.android.presentation.viewmodel.ClubViewModel
import com.uwconnect.android.presentation.viewmodel.LogoutViewModel
import org.uwconnect.models.UpdateClubRequest

@Composable
fun ClubProfileScreen(
    onNavigateToClubHomeScreen: () -> Unit,
    onNavigateToCreateEventScreen: () -> Unit,
    onNavigateToManageEventScreen: () -> Unit,
    onLogoutSuccessNavigation: () -> Unit,
    clubViewModel: ClubViewModel = hiltViewModel(),
    logoutViewModel: LogoutViewModel = hiltViewModel()
) {
    val profile by clubViewModel.profile.collectAsState()
    LaunchedEffect(Unit) {
        clubViewModel.fetchProfile()
    }
    var isEditMode by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var NameFieldFail by remember { mutableStateOf(false) }
    var editedName by remember(profile) { mutableStateOf(profile?.name ?: "") }
    var editedDescription by remember(profile) { mutableStateOf(profile?.description ?: "") }
    var instagramLink by remember(profile) { mutableStateOf(profile?.instagram) }
    var discordLink by remember(profile) { mutableStateOf(profile?.discord) }
    var facebookLink by remember(profile) { mutableStateOf(profile?.facebook) }
    var originalName by remember { mutableStateOf("") }
    var originalDescription by remember { mutableStateOf("") }
    var originalInstagram by remember { mutableStateOf("") }
    var originalDiscord by remember { mutableStateOf("") }
    var originalFacebook by remember { mutableStateOf("") }
    val isLoading by clubViewModel.isLoading.collectAsState()
    val operationResult by clubViewModel.operationResult.collectAsState()

    val handleSubmissionAttempt = {
        if (editedName.isEmpty()) {
            NameFieldFail = true
            showConfirmationDialog = false
        } else {
            showConfirmationDialog = true
        }
    }
    LaunchedEffect(profile) {
        profile?.let {
            if (!isEditMode) {
                originalName = it.name
                originalDescription = it.description ?: ""
                originalInstagram = it.instagram ?: ""
                originalDiscord = it.discord ?: ""
                originalFacebook = it.facebook ?: ""
                editedName = it.name
                editedDescription = it.description ?: ""
                instagramLink = it.instagram
                discordLink = it.discord
                facebookLink = it.facebook
            }
        }
    }
    val performUpdate = {
        clubViewModel.clearOperationResult()
        clubViewModel.updateProfile(
            UpdateClubRequest(
                name = editedName,
                description = editedDescription,
                facebook = facebookLink,
                instagram = instagramLink,
                discord = discordLink
            )
        )
    }
    LaunchedEffect(operationResult) {
        operationResult?.onSuccess {
            showConfirmationDialog = false
            isEditMode = false
        }?.onFailure {
            NameFieldFail = true
            showConfirmationDialog = false
        }
    }
    CustomAlertDialogue(
        showDialog = isEditMode && NameFieldFail,
        title = "Name Taken/Empty or Failure",
        message = "Club name is already taken or empty or failed to update.",
        onDismissRequest = { NameFieldFail = false },
        singleButton = true,
        singleButtonText = "OK",
        singleButtonAction = { NameFieldFail = false }
    )

    ConfirmationDialog(
        showDialog = showConfirmationDialog,
        title = "Confirm Creation",
        message = "Are you sure you want to update the profile?",
        onConfirm = {
            performUpdate()
        },
        onCancel = {
            showConfirmationDialog = false
        }
    )
    NavigationHelper(
        shouldNavigate = { logoutViewModel.logoutState.isLogoutSuccess },
        destination = { onLogoutSuccessNavigation() }
    )
    Scaffold(
        bottomBar = {
            ClubNavBar(
                contentScreen = ClubBottomNavType.Profile,
                onNavigateToClubHomeScreen = { onNavigateToClubHomeScreen() },
                onNavigateToCreateEventScreen = { onNavigateToCreateEventScreen() },
                onNavigateToManageEventScreen = { onNavigateToManageEventScreen() },
                onNavigateToClubProfileScreen = { }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(DarkBlue)
                .pointerInput(Unit) {
                    detectTapGestures(onPress = {
                        logoutViewModel.onClearErrorMessage()
                    })
                }
        ) {
            val lazyColumnModifier = if (!isEditMode) Modifier.padding(bottom = 130.dp) else Modifier.padding(bottom = 70.dp)
            LazyColumn(modifier = lazyColumnModifier) {
                item {
                    Text(
                        text = "Club Profile",
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
                            description = "Club Name",
                            placeholder = "Enter Club Name",
                            textValue = editedName,
                            textColor = White,
                            cursorColor = LightBlue,
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
                            description = "Club Description",
                            placeholder = "Enter Club Description",
                            textValue = editedDescription,
                            textColor = White,
                            cursorColor = LightBlue,
                            onValueChanged = { editedDescription = it },
                            leadingIcon = Icons.Rounded.Description,
                            modifier = Modifier.padding(horizontal = 10.dp),
                        )
                    }
                    item {
                        TextEntryModule(
                            description = "Instagram",
                            placeholder = "Enter Instagram URL (Optional)",
                            textValue = instagramLink ?: "",
                            textColor = White,
                            cursorColor = LightBlue,
                            onValueChanged = { instagramLink = it },
                            trailingIcon = null,
                            onTrailingIconClick = null,
                            leadingIcon = Icons.Rounded.ConnectWithoutContact,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                    item {
                        TextEntryModule(
                            description = "Facebook",
                            placeholder = "Enter Facebook URL (Optional)",
                            textValue = facebookLink ?: "",
                            textColor = White,
                            cursorColor = LightBlue,
                            onValueChanged = { facebookLink = it },
                            trailingIcon = null,
                            onTrailingIconClick = null,
                            leadingIcon = Icons.Rounded.ConnectWithoutContact,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                    item {
                        TextEntryModule(
                            description = "Discord",
                            placeholder = "Enter Discord URL (Optional)",
                            textValue = discordLink ?: "",
                            textColor = White,
                            cursorColor = LightBlue,
                            onValueChanged = { discordLink = it },
                            trailingIcon = null,
                            onTrailingIconClick = null,
                            leadingIcon = Icons.Rounded.ConnectWithoutContact,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                } else {
                    item { Spacer(modifier = Modifier.height(5.dp)) }
                    item {
                        Text(
                            text = "Club Name",
                            color = LightBlue,
                            fontSize = 28.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(start = 28.dp, bottom = 4.dp)
                        )
                    }
                    item {
                        Text(
                            text = editedName,
                            color = White,
                            fontSize = 13.sp,
                            fontFamily = PoppinsFamily,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(start = 28.dp, bottom = 16.dp, end = 14.dp)
                        )
                    }
                    item { Spacer(modifier = Modifier.height(15.dp)) }
                    if (editedDescription.isNotEmpty()) {
                        item {
                            Text(
                                text = "Club Description",
                                color = LightBlue,
                                fontSize = 28.sp,
                                fontFamily = PoppinsFamily,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(start = 28.dp, bottom = 4.dp)
                            )
                        }
                        item {
                            Text(
                                text = editedDescription,
                                color = White,
                                fontSize = 13.sp,
                                fontFamily = PoppinsFamily,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(start = 28.dp, bottom = 16.dp, end = 14.dp)
                            )
                        }
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
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        AuthenticationButton(
                            text = "Cancel",
                            backgroundColor = Gray,
                            contentColor = White,
                            enabled = true,
                            onButtonClick = {
                                editedName = originalName
                                editedDescription = originalDescription
                                instagramLink = originalInstagram.takeIf { it.isNotEmpty() }
                                discordLink = originalDiscord.takeIf { it.isNotEmpty() }
                                facebookLink = originalFacebook.takeIf { it.isNotEmpty() }
                                isEditMode = false
                                NameFieldFail = false
                                isEditMode = false
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    SocialMediaLinksRow(instagramLink, discordLink, facebookLink)
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
                                isEditMode = true
                                originalName = editedName
                                originalDescription = editedDescription
                                originalInstagram = instagramLink ?: ""
                                originalDiscord = discordLink ?: ""
                                originalFacebook = facebookLink ?: ""
                            },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )
                        AuthenticationButton(
                            text = "Logout",
                            backgroundColor = DarkRed,
                            contentColor = White,
                            enabled = true,
                            onButtonClick = logoutViewModel::onLogoutClick,
                            isLoading = logoutViewModel.logoutState.isLoading,
                            modifier = Modifier.weight(1f).padding(bottom = 0.dp)
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

@SuppressLint("QueryPermissionsNeeded")
fun openUrl(context: Context, url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}