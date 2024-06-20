package com.uwconnect.android.domain.repository

import com.uwconnect.android.domain.model.AccountType
import org.uwconnect.models.LoginResponse

interface AuthRepository {
    suspend fun signup(name: String, email: String, password: String, accountType: AccountType): Boolean
    suspend fun logout(): Boolean
    suspend fun login(email: String, password: String, accountType: AccountType): String
}