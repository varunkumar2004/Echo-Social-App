package com.varunkumar.echo_social_app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.utils.Constants.Companion.POSTS
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun getAllPosts(): List<Post> {
        var posts = emptyList<Post>()
        val currUser = auth.currentUser?.email

        currUser?.let { curr ->
            firestore.collection(POSTS).get().await().forEach {
                val post = it.toObject(Post::class.java)
                if (post.email != curr) {
                    posts = posts + post
                }
            }
        }

        return posts
    }
}