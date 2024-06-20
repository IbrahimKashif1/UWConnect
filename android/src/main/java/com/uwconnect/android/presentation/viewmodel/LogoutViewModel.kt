package com.uwconnect.android.presentation.viewmodel

import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.repository.AuthRepository
import com.uwconnect.android.presentation.state.LogoutState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {

    var logoutState by mutableStateOf(LogoutState())

    fun onLogoutClick() {
        logoutState = logoutState.copy(isLoading = true)
        viewModelScope.launch {
            val logoutResult = authRepository.logout()
            logoutState = logoutState.copy(isLogoutSuccess = logoutResult)
            if (!logoutResult) {
                logoutState = logoutState.copy(errorMessageLogout = "An error occurred")
            }
            logoutState = logoutState.copy(isLoading = false)
        }
    }

    fun onClearErrorMessage() {
        logoutState = logoutState.copy(errorMessageLogout = null)
    }
}


