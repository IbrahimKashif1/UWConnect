package com.uwconnect.android.presentation.state

data class LogoutState(
    val isLoading: Boolean = false,
    val isLogoutSuccess: Boolean = false,
    var errorMessageLogout: String? = null        // Related to network/API
)