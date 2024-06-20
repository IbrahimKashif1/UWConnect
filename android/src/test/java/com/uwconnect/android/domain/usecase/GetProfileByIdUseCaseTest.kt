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
class GetProfileByIdUseCaseTest {
    private lateinit var repository: ClubRepository
    private lateinit var getProfileByIdUseCase: GetProfileByIdUseCase

    @Before
    fun setUp() {
        repository = mock()
        getProfileByIdUseCase = GetProfileByIdUseCaseImpl(repository)
    }

    @Test
    fun `invoke with valid clubId returns Club`() = runBlockingTest {
        val clubId = 1
        val mockedClub = Club(
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
        whenever(repository.getProfileById(clubId)).thenReturn(mockedClub)

        val result = getProfileByIdUseCase(clubId)

        verify(repository).getProfileById(clubId)
        assert(result == mockedClub)
    }

    @Test
    fun `invoke with invalid clubId returns null`() = runBlockingTest {
        val invalidClubId = 2
        whenever(repository.getProfileById(invalidClubId)).thenReturn(null)

        val result = getProfileByIdUseCase(invalidClubId)

        verify(repository).getProfileById(invalidClubId)
        assert(result == null)
    }
}