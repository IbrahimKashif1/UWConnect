package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.uwconnect.models.CreateAnnouncementRequest

class CreateClubAnnouncementUseCaseTest {
    private val mockRepository: ClubRepository = mock()
    private val useCase = CreateClubAnnouncementUseCaseImpl(mockRepository)

    @Test
    fun `invoke - success`() = runBlocking {
        val announcement = CreateAnnouncementRequest(
            "Title", "Description",
            LocalDateTime(2021, 1, 1, 0, 0)
        )
        whenever(mockRepository.createAnnouncement(announcement)).thenReturn(Result.success(Unit))
        val result = useCase.invoke(announcement)
        assertTrue(result.isSuccess)
    }

    @Test
    fun `invoke - failure`() = runBlocking {
        val announcement = CreateAnnouncementRequest("Title", "Description",
            LocalDateTime(2021, 1, 1, 0, 0)
        )
        val exception = RuntimeException("Error creating announcement")
        whenever(mockRepository.createAnnouncement(announcement)).thenReturn(Result.failure(exception))

        val result = useCase.invoke(announcement)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
