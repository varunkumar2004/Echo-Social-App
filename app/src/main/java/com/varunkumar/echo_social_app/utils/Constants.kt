package com.varunkumar.echo_social_app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Constants {
    companion object {
        const val Users: String = "Users"
        const val Posts: String = "Posts"
        const val Comments: String = "Comments"
        const val profile_img: String = "profile_img"
        const val Followers: String = "Followers"
        const val Bookmarks: String = "Bookmarks"
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
