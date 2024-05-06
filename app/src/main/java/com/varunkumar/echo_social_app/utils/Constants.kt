package com.varunkumar.echo_social_app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Constants {
    companion object {
        const val USERS: String = "Users"
        const val POSTS: String = "Posts"
        const val COMMENTS: String = "Comments"
        const val PROFILEIMG: String = "profile_img"
        const val FOLLOWERS: String = "Followers"
        const val BOOKMARKS: String = "Bookmarks"
    }
}

fun getCurrentTimestamp(): String {
    return SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
}

fun extractTimestamp(time: String): String? {
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    val date = dateFormat.parse(time)

    val readableDateFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
    val readableDate = date?.let { readableDateFormat.format(it) }
    return readableDate
}
