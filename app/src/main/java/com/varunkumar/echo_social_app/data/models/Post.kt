package com.varunkumar.echo_social_app.data.models

data class Post(
    val name: String = "",
    val email: String = "",
    val profile_image: String? = null,
    val image: String? = null,
    val caption: String = "",
    val timestamp: String = ""
)
