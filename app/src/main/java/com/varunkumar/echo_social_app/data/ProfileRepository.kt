package com.varunkumar.echo_social_app.data

import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.Posts
import com.varunkumar.echo_social_app.utils.Constants.Companion.Users
import com.varunkumar.echo_social_app.utils.Result
import com.varunkumar.echo_social_app.utils.getCurrentTimestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun registerUserWithEmail(user: User, password: String): Result<Boolean> = try {
        auth.createUserWithEmailAndPassword(user.email, password).await()
        registerFirestore(user)
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun loginUserWithEmail(email: String, password: String): Result<Boolean> = try {
        auth.signInWithEmailAndPassword(email, password).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    private suspend fun registerFirestore(user: User): Result<Boolean> = try {
        val timestamp = getCurrentTimestamp()
        val currUser = user.copy(timestamp = timestamp)

        firestore.collection(Users).document(user.email).set(currUser).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getCurrentUser(): User? {
        val uid = auth.currentUser?.email

        uid?.let {
            return firestore.collection(Users).document(uid).get().await()
                .toObject(User::class.java)
        }

        return null
    }

    suspend fun getUser(email: String): User? {
        return firestore.collection(Users).document(email).get().await().toObject(User::class.java)
    }

    suspend fun getUserPosts(email: String): List<Post> {
        var posts = emptyList<Post>()

        firestore.collection(Users).document(email).collection(Posts).get().await().forEach {
            val post = it.toObject(Post::class.java)
            posts = posts + post
        }

        // TODO reverse the list
        return posts
    }

    fun logoutUser(): Result<Boolean> = try {
        auth.signOut()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
