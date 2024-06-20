package com.uwconnect.android.domain.repository

import com.uwconnect.android.domain.model.ClubEvent
import org.uwconnect.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {
    @POST("/auth/login")
    suspend fun loginAuthMember(@Body body: LoginRequest): Response<LoginResponse>

    @POST("/auth/signup")
    suspend fun signupAuthMember(@Body body: SignupRequest): Response<SignupResponse>

    @POST("/auth/club/login")
    suspend fun loginAuthClub(@Body body: LoginRequest): Response<LoginResponse>

    @POST("/auth/club/signup")
    suspend fun signupAuthClub(@Body body: SignupRequest): Response<SignupResponse>

    @GET("/club/all")
    suspend fun getAllClubs(): Response<ClubsResponse>

    @GET("/club/profile/{id}")
    suspend fun getClubByID(@Path("id") id: Int): Response<ClubResponse>

    @POST("/club/{id}/join")
    suspend fun joinClub(@Path("id") id: Int): Response<Unit>

    @POST("/club/{id}/leave")
    suspend fun leaveClub(@Path("id") id: Int): Response<Unit>

    @GET("/club/profile")
    suspend fun getClubProfile(): Response<ClubResponse>

    @POST("/club/create-announcement")
    suspend fun createClubAnnouncement(@Body body: CreateAnnouncementRequest): Response<Unit>

    // get all user events (for both club and member)
    @GET("/user/profile")
    suspend fun getUserProfile(): Response<UserResponse>

    @GET("/user/announcements")
    suspend fun getUserAnnouncements(): Response<AnnouncementsResponse>

    @POST("/user/token")
    suspend fun registerToken(@Body tokenRequest: RegisterTokenRequest): Response<Unit>

    // get club event by id
    @GET("/event/{id}")
    suspend fun getEventById(@Path("id") eventId: Int): Response<EventResponse>

    @POST("/event/create")
    suspend fun createEvent(@Body event: CreateEventRequest): Response<Unit>

    @GET("club/events")
    suspend fun getAllEvents(): Response<List<ClubEvent>>

    @POST("/event/{id}/delete")
    suspend fun deleteEvent(@Path("id") eventId: Int): Response<Unit>

    @POST("/event/{id}/update")
    suspend fun updateEvent(@Path("id") eventId: Int, @Body event: UpdateEventRequest): Response<Unit>

    // update club profile
    @POST("/club/update")
    suspend fun updateClubProfile(@Body clubProfile: UpdateClubRequest): Response<Unit>

    @POST("/user/update")
    suspend fun updateUserProfile(@Body userProfile: UpdateUserRequest): Response<Unit>

    @POST("/event/{id}/join")
    suspend fun joinEvent(@Path("id") eventId: Int): Response<Unit>

    @POST("/event/{id}/leave")
    suspend fun leaveEvent(@Path("id") eventId: Int): Response<Unit>
}