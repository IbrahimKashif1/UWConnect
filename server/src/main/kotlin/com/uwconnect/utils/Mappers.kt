package com.uwconnect.utils

import org.uwconnect.models.*
import org.jetbrains.exposed.sql.SizedIterable
import com.uwconnect.dao.Club as ClubData
import com.uwconnect.dao.User as UserData
import com.uwconnect.dao.Event as EventData
import com.uwconnect.dao.Announcement as AnnouncementData

fun userMapper(user: UserData): User {
    return User( user.id.value, user.name, user.email, user.bio, null, null )
}

fun usersMapper(users: SizedIterable<UserData>): List<User> {
    return users.map {
        User( it.id.value, it.name, it.email, it.bio, null, null )
    }
}

fun clubMapper(club: ClubData): Club {
    return Club ( club.id.value, club.name, club.email, club.description, club.facebook, club.instagram, club.discord, null, null, null )
}

fun clubsMapper(clubs: SizedIterable<ClubData>): List<Club> {
    return clubs.map {
        Club( it.id.value, it.name, it.email, it.description, it.facebook, it.instagram, it.discord, null, null, null )
    }
}

fun eventMapper(event: EventData): Event {
    return Event ( event.id.value, event.title, event.description, event.location, event.link, event.color, clubMapper(event.club), event.start, event.end, null )
}

fun eventsMapper(events: SizedIterable<EventData>): List<Event> {
    return events.map {
        Event( it.id.value, it.title, it.description, it.location, it.link, it.color, clubMapper(it.club), it.start, it.end, null )
    }
}

fun announcementMapper(announcement: AnnouncementData): Announcement {
    return Announcement ( announcement.id.value, announcement.title, announcement.description, clubMapper(announcement.club), announcement.timestamp )
}

fun announcementsMapper(announcements: SizedIterable<AnnouncementData>): List<Announcement> {
    return announcements.map {
        Announcement( it.id.value, it.title, it.description, clubMapper(it.club), it.timestamp )
    }
}

fun updatesMapper(announcements: List<AnnouncementData>): List<Announcement> {
    return announcements.map {
        Announcement( it.id.value, it.title, it.description, clubMapper(it.club), it.timestamp )
    }
}



