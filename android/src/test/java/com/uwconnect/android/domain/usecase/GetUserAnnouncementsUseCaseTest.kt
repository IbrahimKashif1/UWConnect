package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDateTime
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import org.uwconnect.models.Announcement
import org.uwconnect.models.Club

class GetUserAnnouncementsUseCaseTest {
    private val mockRepository = mock(UserRepository::class.java)
    private val getUserAnnouncementsUseCase = GetUserAnnouncementUseCaseImpl(mockRepository)

    @Test
    fun `invoke returns non-empty list on success`() = runBlocking {
        val expectedAnnouncements = listOf(
            Announcement(1, "Announcement 1", "Content 1",
                Club(
                    id = 2,
                    name = "Book Club",
                    email = "bookclub@example.com",
                    description = "A club for people who love reading.",
                    facebook = "bookclubfb",
                    instagram = null,
                    discord = null,
                    members = listOf(),
                    events = listOf(),
                    announcements = listOf()
                ),
                LocalDateTime(2021, 1, 1, 1, 1)),
            Announcement(2, "Announcement 2", "Content 2",
                Club(
                    id = 2,
                    name = "Book Club",
                    email = "bookclub@example.com",
                    description = "A club for people who love reading.",
                    facebook = "bookclubfb",
                    instagram = null,
                    discord = null,
                    members = listOf(),
                    events = listOf(),
                    announcements = listOf()
                ),
                LocalDateTime(2021, 2, 2, 2, 2)),
        )
        `when`(mockRepository.getAnnouncements()).thenReturn(expectedAnnouncements)

        val result = getUserAnnouncementsUseCase.invoke()

        verify(mockRepository).getAnnouncements()
        assertEquals(expectedAnnouncements, result)
        assertTrue(result.isNotEmpty())
    }

    @Test
    fun `invoke returns empty list when no announcements`() = runBlocking {
        `when`(mockRepository.getAnnouncements()).thenReturn(emptyList())

        val result = getUserAnnouncementsUseCase.invoke()

        verify(mockRepository).getAnnouncements()
        assertTrue(result.isEmpty())
    }
}
