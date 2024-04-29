package com.varunkumar.echo_social_app.data.models

import kotlinx.serialization.Serializable

data class Post(
    val name:String = "",
    val email: String = "",
    val image: String? = null,
    val caption: String = "",
    val timestamp: String = "",
    val bookmarks: Int = 0
)
