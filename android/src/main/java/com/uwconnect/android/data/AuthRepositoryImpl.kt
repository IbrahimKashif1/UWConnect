package com.uwconnect.android.data

import com.uwconnect.android.domain.model.AccountType
import com.uwconnect.android.domain.repository.AuthRepository
import com.uwconnect.android.util.TokenManager
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.uwconnect.models.LoginRequest
import org.uwconnect.models.SignupRequest

class AuthRepositoryImpl: AuthRepository {
    override suspend fun login(email: String, password: String, accountType: AccountType): String {
        try {
            val userType: String = accountType.name.uppercase()
            val response = coroutineScope {
                val requestBody = LoginRequest(email, password)
                val request = async {
                    if (userType == "MEMBER") {
                        ApiClient.apiService.loginAuthMember(requestBody)
                    } else {
                        ApiClient.apiService.loginAuthClub(requestBody)
                    }
                }
                request.await()
            }

            if (response.isSuccessful) {
                val jwt = response.body()?.jwt ?: ""

                // Store JWT with dataStore
                TokenManager.saveJwt(jwt)
                delay(1000)
                return jwt
            } else {
                return ""
            }
        } catch (e: Exception) {
            println("Error logging in: ${e.message}")
            // Handle the error or log it
            return ""
        }
    }

    override suspend fun signup(name: String, email: String, password: String, accountType: AccountType): Boolean {
        try {
            val response = coroutineScope {
                val userType: String = accountType.name.uppercase()
                val requestBody = SignupRequest(name, email, password)

                val request = async {
                    if (userType == "MEMBER") {
                        ApiClient.apiService.signupAuthMember(requestBody)
                    } else {
                        ApiClient.apiService.signupAuthClub(requestBody)
                    }
                }
                request.await()
            }

            if (response.isSuccessful) {
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            println("Error signing up: ${e.message}")
            // Handle the error or log it
            return false
        }
    }

    override suspend fun logout(): Boolean {
        delay(1000)
        TokenManager.saveJwt("")
        return true
    }
}