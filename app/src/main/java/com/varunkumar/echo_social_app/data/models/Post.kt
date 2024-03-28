package com.varunkumar.echo_social_app.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val email: String = "",
    val image: String? = null,
    val caption: String = "",
    val timestamp: String = "",
    val bookmarks: Int = 0
)

val dummyPosts = listOf(
    Post(
        email = "john@example.com",
        image = "post1.jpg",
        caption = "Beautiful sunset view",
        timestamp = "2024-03-25 18:00",
        bookmarks = 50
    ),
    Post(
        email = "alice@example.com",
        image = "post2.jpg",
        caption = "Exploring nature",
        timestamp = "2024-03-25 19:30",
        bookmarks = 30
    ),
    Post(
        email = "bob@example.com",
        image = "post3.jpg",
        caption = "Delicious food",
        timestamp = "2024-03-25 20:45",
        bookmarks = 20
    )
)
