package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.ClubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.uwconnect.models.Club
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlinx.coroutines.test.runBlockingTest

@ExperimentalCoroutinesApi
class GetAllClubsUseCaseTest {
    private lateinit var clubRepository: ClubRepository
    private lateinit var getAllClubsUseCase: GetAllClubsUseCase

    @Before
    fun setUp() {
        clubRepository = mock()
        getAllClubsUseCase = GetAllClubsUseCaseImpl(clubRepository)
    }

    @Test
    fun `invoke calls repository getAll and returns list of clubs`() = runBlockingTest {
        val mockedClubs = listOf(
            Club(
                id = 1,
                name = "Chess Club",
                email = "chessclub@example.com",
                description = "A club for chess enthusiasts.",
                facebook = null,
                instagram = "chessclubinsta",
                discord = "chessclubdiscord",
                members = listOf(),
                events = listOf(),
                announcements = listOf()
            ),
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
            )
        )
        whenever(clubRepository.getAll()).thenReturn(mockedClubs)

        val result = getAllClubsUseCase()

        verify(clubRepository).getAll()
        assert(result == mockedClubs)
    }
}