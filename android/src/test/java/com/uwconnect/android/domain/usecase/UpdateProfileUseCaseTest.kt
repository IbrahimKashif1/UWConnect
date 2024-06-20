package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import org.uwconnect.models.UpdateClubRequest

class UpdateProfileUseCaseTest {
    private val mockRepository = mock(ClubRepository::class.java)
    private val updateProfileUseCase = UpdateProfileUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns success when updateProfile is successful`() = runBlocking {
        val updateClubRequest = UpdateClubRequest(
            "Updated Club Name",
            "Updated Description",
            "New FB",
            "New Insta",
            "New Discord"
        )
        `when`(mockRepository.updateProfile(updateClubRequest)).thenReturn(Result.success(Unit))

        val result = updateProfileUseCase.invoke(updateClubRequest)

        verify(mockRepository).updateProfile(updateClubRequest)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when updateProfile is unsuccessful`() = runBlocking {
        val updateClubRequest = UpdateClubRequest(
            "Updated Club Name",
            "Updated Description",
            "New FB",
            "New Insta",
            "New Discord"
        )
        val exception = RuntimeException("Error updating profile")
        `when`(mockRepository.updateProfile(updateClubRequest)).thenReturn(Result.failure(exception))

        val result = updateProfileUseCase.invoke(updateClubRequest)

        verify(mockRepository).updateProfile(updateClubRequest)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
