package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class LeaveClubUseCaseTest {
    private val mockRepository = mock(ClubRepository::class.java)
    private val leaveClubUseCase = LeaveClubUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns success when leaveClub is successful`() = runBlocking {
        val clubId = 1
        `when`(mockRepository.leaveClub(clubId)).thenReturn(Result.success(Unit))

        val result = leaveClubUseCase.invoke(clubId)

        verify(mockRepository).leaveClub(clubId)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when leaveClub is unsuccessful`() = runBlocking {
        val clubId = 1
        val exception = RuntimeException("Error leaving club")
        `when`(mockRepository.leaveClub(clubId)).thenReturn(Result.failure(exception))

        val result = leaveClubUseCase.invoke(clubId)

        verify(mockRepository).leaveClub(clubId)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
