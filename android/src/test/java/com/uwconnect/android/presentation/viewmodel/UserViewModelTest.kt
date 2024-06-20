package com.uwconnect.android.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.uwconnect.android.domain.usecase.GetProfileUserUseCase
import com.uwconnect.android.domain.usecase.GetUserAnnouncementsUseCase
import com.uwconnect.android.domain.usecase.UpdateProfileUserUseCase
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.uwconnect.models.UpdateUserRequest
import org.uwconnect.models.User
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var viewModel: UserViewModel
    private val getProfileUserUseCase: GetProfileUserUseCase = mock()
    private val updateProfileUserUseCase: UpdateProfileUserUseCase = mock()
    private val getUserAnnouncementsUseCase: GetUserAnnouncementsUseCase = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(getProfileUserUseCase,
            updateProfileUserUseCase,
            getUserAnnouncementsUseCase
        )
    }

    @Test
    fun `fetchProfile should update profile on success`() = runBlockingTest {
        val user = User(id = 4, name = "name", email = "email@email.com", bio = "bio", clubs = emptyList(), events = emptyList())
        whenever(getProfileUserUseCase.invoke()).thenReturn(user)

        viewModel.profile.test {
            viewModel.fetchProfile()
            awaitItem()
            assertEquals(user, awaitItem())
        }
    }

    @Test
    fun `fetchProfile should handle errors`() = runBlockingTest {
        whenever(getProfileUserUseCase.invoke()).thenThrow(RuntimeException("Failed"))
        val items = mutableListOf<User?>()
        val job = launch { viewModel.profile.collect { items.add(it) } }
        viewModel.fetchProfile()
        advanceUntilIdle()
        assertTrue(items.contains(null))
        job.cancel()
    }

    @Test
    fun `updateProfile should update result on success`() = runBlockingTest {
        val updateResult = Result.success(Unit)
        val updateUserRequest = UpdateUserRequest(name = "name", bio = "bio")
        whenever(updateProfileUserUseCase.invoke(updateUserRequest)).thenReturn(updateResult)

        viewModel.updateResult.test {
            viewModel.updateProfile(updateUserRequest)
            awaitItem()
            assertEquals(updateResult, awaitItem())
        }
    }

    @Test
    fun `updateProfile should handle errors`() = runBlockingTest {
        val error = RuntimeException("Error")
        val updateUserRequest = UpdateUserRequest(name = "name", bio = "bio")
        whenever(updateProfileUserUseCase.invoke(updateUserRequest)).thenReturn(Result.failure(error))

        viewModel.updateResult.test {
            viewModel.updateProfile(updateUserRequest)
            awaitItem()
            assertEquals(Result.failure<Unit>(error), awaitItem())
        }
    }

    @Test
    fun `clearUpdateResult should reset updateResult to null`() = runBlockingTest {
        viewModel.updateProfile(UpdateUserRequest(name = "name_tmp", bio = "bio_tmp"))
        viewModel.clearUpdateResult()
        assertNull(viewModel.updateResult.value)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
}
