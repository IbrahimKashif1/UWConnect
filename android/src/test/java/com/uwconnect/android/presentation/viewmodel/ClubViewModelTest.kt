package com.uwconnect.android.presentation.viewmodel

import junit.framework.TestCase.assertTrue
import kotlinx.datetime.LocalDateTime
import org.uwconnect.models.CreateAnnouncementRequest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.viewModelScope
import com.uwconnect.android.domain.usecase.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.uwconnect.models.Club
import org.uwconnect.models.UpdateClubRequest

@ExperimentalCoroutinesApi
class ClubViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher();
    private lateinit var viewModel: ClubViewModel
    private val getProfileUseCase: GetProfileUseCase = mock()
    private val getProfileByIdUseCase: GetProfileByIdUseCase = mock()
    private val getAllClubsUseCase: GetAllClubsUseCase = mock()
    private val updateProfileUseCase: UpdateProfileUseCase = mock()
    private val joinClubUseCase: JoinClubUseCase = mock()
    private val leaveClubUseCase: LeaveClubUseCase = mock()
    private val createClubAnnouncementUseCase: CreateClubAnnouncementUseCase = mock()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ClubViewModel(
            getProfileUseCase,
            getProfileByIdUseCase,
            getAllClubsUseCase,
            updateProfileUseCase,
            joinClubUseCase,
            leaveClubUseCase,
            createClubAnnouncementUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines();
    }

    @Test
    fun `fetchProfile success`() = runBlockingTest {
        val expectedProfile = Club(1,
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
        whenever(getProfileUseCase.invoke()).thenReturn(expectedProfile)

        viewModel.viewModelScope.launch {
            viewModel.profile.collectLatest { profile ->
                if (profile == null) {
                    assertFalse(false)
                } else {
                    assertEquals(expectedProfile, profile)
                }
            }
        }
        viewModel.fetchProfile()
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `fetchProfileById success`() = runBlockingTest {
        val clubId = 1
        val expectedProfile = Club(1,
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
        whenever(getProfileByIdUseCase.invoke(clubId)).thenReturn(expectedProfile)

        viewModel.viewModelScope.launch {
            viewModel.queryProfile.collectLatest { profile ->
                if (profile == null) {
                    assertFalse(false)
                } else {
                    assertEquals(expectedProfile, profile)
                }
            }
        }
        viewModel.fetchProfileById(clubId)
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `fetchAll success`() = runBlockingTest {
        val expectedClubs = listOf(
            Club(1,
                "Club Name",
                "Club Email",
                "Club Description",
                "FB",
                "IG",
                "DS",
                emptyList(),
                emptyList(),
                emptyList(),
            ),
            Club(2,
                "Club Name 2",
                "Club Email 2",
                "Club Description 2",
                "FB 2",
                "IG 2",
                "DS 2",
                emptyList(),
                emptyList(),
                emptyList(),
            )
        )
        whenever(getAllClubsUseCase.invoke()).thenReturn(expectedClubs)

        viewModel.viewModelScope.launch {
            viewModel.clubs.collectLatest { clubs ->
                assertEquals(expectedClubs, clubs)
            }
        }
        viewModel.fetchAll()
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `updateProfile success`() = runBlockingTest {
        val profileUpdateRequest = UpdateClubRequest(
            "New Club Name",
            "New Club Description",
            "New FB",
            "New IG",
            "New DS"
        )
        whenever(updateProfileUseCase.invoke(profileUpdateRequest)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.operationResult.collectLatest { result ->
                assertTrue(result?.isSuccess == true)
            }
        }
        viewModel.updateProfile(profileUpdateRequest)
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `join club success`() = runBlockingTest {
        val clubId = 1
        whenever(joinClubUseCase.invoke(clubId)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.operationResult.collectLatest { result ->
                assertTrue(result?.isSuccess == true)
            }
        }
        viewModel.join(clubId)
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `leave club success`() = runBlockingTest {
        val clubId = 1
        whenever(leaveClubUseCase.invoke(clubId)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.operationResult.collectLatest { result ->
                assertTrue(result?.isSuccess == true)
            }
        }
        viewModel.leave(clubId)
        assertTrue(viewModel.isLoading.value.not())
    }

    @Test
    fun `create announcement success`() = runBlockingTest {
        val announcementRequest = CreateAnnouncementRequest(
            "Announcement Title",
            "Announcement Details",
            LocalDateTime(2021, 1, 1, 0, 0)
        )
        whenever(createClubAnnouncementUseCase.invoke(announcementRequest)).thenReturn(Result.success(Unit))

        viewModel.viewModelScope.launch {
            viewModel.operationResult.collectLatest { result ->
                assertTrue(result?.isSuccess == true)
            }
        }
        viewModel.createAnnouncement(announcementRequest)
        assertTrue(viewModel.isLoading.value.not())
    }
}
