package com.uwconnect.android.domain.usecase

import com.uwconnect.android.domain.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import org.uwconnect.models.User

class GetProfileUserUseCaseTest {
    private val mockUserRepository = mock(UserRepository::class.java)
    private val getProfileUserUseCase = GetProfileUserUseCaseImpl(mockUserRepository)

    @Test
    fun `invoke returns User on success`() = runBlocking {
        val expectedUser = User(
            id = 4,
            name = "name",
            email = "email@email.com",
            bio = "bio",
            clubs = emptyList(),
            events = emptyList()
        )

        `when`(mockUserRepository.getProfile()).thenReturn(expectedUser)

        val result = getProfileUserUseCase.invoke()

        verify(mockUserRepository).getProfile()
        assertEquals(expectedUser, result)
    }

    @Test
    fun `invoke returns null on failure`() = runBlocking {
        `when`(mockUserRepository.getProfile()).thenReturn(null)

        val result = getProfileUserUseCase.invoke()

        verify(mockUserRepository).getProfile()
        assertNull(result)
    }
}
