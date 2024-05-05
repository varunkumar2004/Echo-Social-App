package com.varunkumar.echo_social_app.data.models

data class User(
    val name: String = "",
    val bio: String? = null,
    val email: String = "",
    val image: String? = null,
    val timestamp: String? = null,
    val followers: Int = 0
)
