package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.UserRepository
import org.uwconnect.models.UpdateUserRequest

interface UpdateProfileUserUseCase {
    suspend operator fun invoke(user: UpdateUserRequest): Result<Unit>
}

class UpdateProfileUserUseCaseImpl(private val userRepository: UserRepository) : UpdateProfileUserUseCase {
    override suspend fun invoke(user: UpdateUserRequest): Result<Unit> = userRepository.updateProfile(user)
}
