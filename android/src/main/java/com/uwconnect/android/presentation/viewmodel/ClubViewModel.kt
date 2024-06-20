package com.uwconnect.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.repository.ClubRepository
import com.uwconnect.android.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.uwconnect.models.Club
import org.uwconnect.models.CreateAnnouncementRequest
import org.uwconnect.models.UpdateClubRequest
import javax.inject.Inject

@HiltViewModel
class ClubViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val getProfileByIdUseCase: GetProfileByIdUseCase,
    private val getAllClubsUseCase: GetAllClubsUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val joinClubUseCase: JoinClubUseCase,
    private val leaveClubUseCase: LeaveClubUseCase,
    private val createClubAnnouncementUseCase: CreateClubAnnouncementUseCase,
) : ViewModel() {
    private val _profile = MutableStateFlow<Club?>(null)
    val profile: StateFlow<Club?> = _profile

    private val _queryProfile = MutableStateFlow<Club?>(null)
    val queryProfile: StateFlow<Club?> = _queryProfile

    private val _clubs = MutableStateFlow<List<Club>>(emptyList())
    val clubs: StateFlow<List<Club>> = _clubs

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _operationResult = MutableStateFlow<Result<Unit>?>(null)
    val operationResult: StateFlow<Result<Unit>?> = _operationResult

    fun fetchProfile() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _profile.value = getProfileUseCase.invoke()
        } catch (e: Exception) {
            // Handle error
        } finally {
            _isLoading.value = false
        }
    }

    fun fetchProfileById(clubId: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            _queryProfile.value = getProfileByIdUseCase.invoke(clubId)
        } catch (e: Exception) {
            // Handle error
        } finally {
            _isLoading.value = false
        }
    }

    fun fetchAll() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _clubs.value = getAllClubsUseCase.invoke()
        } catch (e: Exception) {
            // Handle error
        } finally {
            _isLoading.value = false
        }
    }

    fun updateProfile(profile: UpdateClubRequest) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = updateProfileUseCase.invoke(profile)
            _operationResult.value = result
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun join(clubId: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = joinClubUseCase.invoke(clubId)
            _operationResult.value = result
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun leave(clubId: Int) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = leaveClubUseCase.invoke(clubId)
            _operationResult.value = result
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun createAnnouncement(announcement: CreateAnnouncementRequest) = viewModelScope.launch {
        _isLoading.value = true
        try {
            val result = createClubAnnouncementUseCase.invoke(announcement)
            _operationResult.value = result
        } catch (e: Exception) {
            _operationResult.value = Result.failure(e)
        } finally {
            _isLoading.value = false
        }
    }

    fun clearOperationResult() {
        _operationResult.value = null
    }
}