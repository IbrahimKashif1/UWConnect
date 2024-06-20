package com.uwconnect.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.uwconnect.models.UpdateUserRequest
import org.uwconnect.models.User
import javax.inject.Inject
import com.uwconnect.android.domain.usecase.GetProfileUserUseCase
import com.uwconnect.android.domain.usecase.GetUserAnnouncementsUseCase
import com.uwconnect.android.domain.usecase.UpdateProfileUserUseCase
import org.uwconnect.models.Announcement

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getProfileUserUseCase: GetProfileUserUseCase,
    private val updateProfileUserUseCase: UpdateProfileUserUseCase,
    private val getUserAnnouncementsUseCase: GetUserAnnouncementsUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _profile = MutableStateFlow<User?>(null)
    val profile: StateFlow<User?> = _profile

    private val _updateResult = MutableStateFlow<Result<Unit>?>(null)
    val updateResult: StateFlow<Result<Unit>?> = _updateResult

    private val _announcements = MutableStateFlow<List<Announcement>>(emptyList())
    val announcements: StateFlow<List<Announcement>> = _announcements

    fun fetchProfile() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _profile.value = getProfileUserUseCase.invoke()
        } catch (e: Exception) {
            _profile.value = null
        } finally {
            _isLoading.value = false
        }
    }

    fun updateProfile(user: UpdateUserRequest) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _updateResult.value = updateProfileUserUseCase.invoke(user)
        } catch (e: Exception) {
            _updateResult.value = Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun getAnnouncements() = viewModelScope.launch {
        try {
            _announcements.value = getUserAnnouncementsUseCase.invoke()
        } catch (e: Exception) {
            // swallow error
        }
    }

    fun clearUpdateResult() {
        _updateResult.value = null
    }
}
