package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.UserRepository
import org.uwconnect.models.User

interface GetProfileUserUseCase {
    suspend operator fun invoke(): User?
}

class GetProfileUserUseCaseImpl(private val userRepository: UserRepository) : GetProfileUserUseCase {
    override suspend fun invoke(): User? = userRepository.getProfile()
}
