package com.uwconnect.android.util

import org.uwconnect.models.User

fun isUserClub(clubId: Int, user: User?): Boolean {
    return user?.clubs?.any { it.id == clubId } == true
}