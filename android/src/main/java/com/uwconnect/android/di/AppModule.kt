package com.uwconnect.android.di

import com.uwconnect.android.data.*
import com.uwconnect.android.domain.repository.*
import com.uwconnect.android.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideValidateLoginUseCase(): ValidateLoginUseCase {
        return ValidateLoginUseCase()
    }

    @Provides
    @Singleton
    fun provideValidateSignupUseCase(): ValidateSignupUseCase {
        return ValidateSignupUseCase()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideEventRepository(): EventRepository {
        return EventRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideClubRepository(): ClubRepository {
        return ClubRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGetEventByIdUseCase(repository: EventRepository): GetEventByIdUseCase {
        return GetEventByIdUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideCreateEventUseCase(repository: EventRepository): CreateEventUseCase {
        return CreateEventUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateEventUseCase(repository: EventRepository): UpdateEventUseCase {
        return UpdateEventUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteEventUseCase(repository: EventRepository): DeleteEventUseCase {
        return DeleteEventUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideJoinEventUseCase(repository: EventRepository): JoinEventUseCase {
        return JoinEventUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideLeaveEventUseCase(repository: EventRepository): LeaveEventUseCase {
        return LeaveEventUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetProfileUseCase(repository: ClubRepository): GetProfileUseCase {
        return GetProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetProfileByIdUseCase(repository: ClubRepository): GetProfileByIdUseCase {
        return GetProfileByIdUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllClubsUseCase(repository: ClubRepository): GetAllClubsUseCase {
        return GetAllClubsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateProfileUseCase(repository: ClubRepository): UpdateProfileUseCase {
        return UpdateProfileUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideJoinClubUseCase(repository: ClubRepository): JoinClubUseCase {
        return JoinClubUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideLeaveClubUseCase(repository: ClubRepository): LeaveClubUseCase {
        return LeaveClubUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideCreateClubAnnouncementUseCase(clubRepository: ClubRepository): CreateClubAnnouncementUseCase {
        return CreateClubAnnouncementUseCaseImpl(clubRepository)
    }

    @Provides
    @Singleton
    fun provideGetProfileUserUseCase(userRepository: UserRepository): GetProfileUserUseCase {
        return GetProfileUserUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideUpdateProfileUserUseCase(userRepository: UserRepository): UpdateProfileUserUseCase {
        return UpdateProfileUserUseCaseImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserAnnouncementsUseCase(userRepository: UserRepository): GetUserAnnouncementsUseCase {
        return GetUserAnnouncementUseCaseImpl(userRepository)
    }
}