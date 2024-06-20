package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*

class JoinClubUseCaseTest {
    private val mockRepository = mock(ClubRepository::class.java)
    private val joinClubUseCase = JoinClubUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns success when joinClub is successful`() = runBlocking {
        val clubId = 1
        `when`(mockRepository.joinClub(clubId)).thenReturn(Result.success(Unit))

        val result = joinClubUseCase.invoke(clubId)

        verify(mockRepository).joinClub(clubId)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke returns failure when joinClub is unsuccessful`() = runBlocking {
        val clubId = 1
        val exception = RuntimeException("Error joining club")
        `when`(mockRepository.joinClub(clubId)).thenReturn(Result.failure(exception))

        val result = joinClubUseCase.invoke(clubId)

        verify(mockRepository).joinClub(clubId)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
