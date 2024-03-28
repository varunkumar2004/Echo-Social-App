package com.varunkumar.echo_social_app.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Constants {
    companion object {
        const val Users: String = "Users"
        const val Posts: String = "Posts"
        const val Comments: String = "Comments"
        const val Profile: String = "pr"
    }
}


fun Context.createImageFile(): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val imageFileName = "JPEG_" + timestamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}

fun getCurrentTimestamp(): String {
    val date = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    return date
}

fun extractTimestamp(time: String): String? {
    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
    val date = dateFormat.parse(time)

    val readableDateFormat = SimpleDateFormat("dd/MM/yy", Locale.US)
    val readableDate = date?.let { readableDateFormat.format(it) }
    return readableDate
}
