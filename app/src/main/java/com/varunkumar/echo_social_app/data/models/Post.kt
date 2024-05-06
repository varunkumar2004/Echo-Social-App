package com.varunkumar.echo_social_app.data.models

data class Post(
    val name: String = "",
    val email: String = "",
    val profileImage: String? = null,
    val image: String? = null,
    val caption: String = "",
    val timestamp: String = ""
)
