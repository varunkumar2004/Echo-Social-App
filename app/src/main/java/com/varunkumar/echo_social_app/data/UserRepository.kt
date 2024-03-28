package com.varunkumar.echo_social_app.data

import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.utils.Constants.Companion.Posts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun getAllPosts(): List<Post> {
        var posts = emptyList<Post>()

        firestore.collection(Posts).get().await().forEach {
            val post = it.toObject(Post::class.java)
            posts = posts + post
        }

        return posts
    }
}