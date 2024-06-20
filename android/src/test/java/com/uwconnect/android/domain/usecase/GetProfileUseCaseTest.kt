package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import org.uwconnect.models.Club

class GetProfileUseCaseTest {
    private val mockRepository = mock(ClubRepository::class.java)
    private val getProfileUseCase = GetProfileUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns Club on success`() = runBlocking {
        val expectedClub = Club(1,
            "Club Name",
            "Club Email",
            "Club Description",
            "FB",
            "IG",
            "DS",
            emptyList(),
            emptyList(),
            emptyList(),
        )
        `when`(mockRepository.getProfile()).thenReturn(expectedClub)

        val result = getProfileUseCase.invoke()

        verify(mockRepository).getProfile()
        assertEquals(expectedClub, result)
    }

    @Test
    fun `invoke returns null on failure`() = runBlocking {
        `when`(mockRepository.getProfile()).thenReturn(null)

        val result = getProfileUseCase.invoke()

        verify(mockRepository).getProfile()
        assertNull(result)
    }
}
