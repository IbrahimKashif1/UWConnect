package com.uwconnect.android.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.util.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _id = MutableStateFlow<Int>(0)
    val id: StateFlow<Int> = _id

    private val _type = MutableStateFlow<String>("")
    val type: StateFlow<String> = _type

    private val _isAuthenticated = MutableStateFlow<Boolean>(false)
    val isAuthenticated = _isAuthenticated

    fun loadAuthState() = viewModelScope.launch {
        _isLoading.value = true

        val (jwtId, jwtType) = TokenManager.parseJwt(TokenManager.getJwt())

        _id.value = jwtId
        _type.value = jwtType
        _isAuthenticated.value = !TokenManager.getJwt().isBlank()

        _isLoading.value = false
    }
}