package com.varunkumar.echo_social_app.data.models

data class Comment(
    val comment: String = "",
    val user: String = "", // email of the one who posted
    val timestamp: String = "",
    val email: String = ""
)