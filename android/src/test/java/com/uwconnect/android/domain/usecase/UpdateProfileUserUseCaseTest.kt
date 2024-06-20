package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import org.uwconnect.models.UpdateUserRequest

class UpdateProfileUserUseCaseTest {
    private val mockUserRepository = mock(UserRepository::class.java)
    private val updateProfileUserUseCase = UpdateProfileUserUseCaseImpl(mockUserRepository)

    @Test
    fun `invoke returns success when updateProfile is successful`() = runBlocking {
        val updateUserRequest = UpdateUserRequest("New Name", "New Bio")
        `when`(mockUserRepository.updateProfile(updateUserRequest)).thenReturn(Result.success(Unit))

        val result = updateProfileUserUseCase.invoke(updateUserRequest)
        verify(mockUserRepository).updateProfile(updateUserRequest)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when updateProfile is unsuccessful`() = runBlocking {
        val updateUserRequest = UpdateUserRequest("New Name", "New Bio")
        val exception = RuntimeException("Error updating user profile")
        `when`(mockUserRepository.updateProfile(updateUserRequest)).thenReturn(Result.failure(exception))

        val result = updateProfileUserUseCase.invoke(updateUserRequest)

        verify(mockUserRepository).updateProfile(updateUserRequest)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
