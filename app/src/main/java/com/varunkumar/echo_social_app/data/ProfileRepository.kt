package com.varunkumar.echo_social_app.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.varunkumar.echo_social_app.data.models.Follower
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.BOOKMARKS
import com.varunkumar.echo_social_app.utils.Constants.Companion.FOLLOWERS
import com.varunkumar.echo_social_app.utils.Constants.Companion.POSTS
import com.varunkumar.echo_social_app.utils.Constants.Companion.USERS
import com.varunkumar.echo_social_app.utils.Constants.Companion.PROFILEIMG
import com.varunkumar.echo_social_app.utils.Result
import com.varunkumar.echo_social_app.utils.getCurrentTimestamp
import kotlinx.coroutines.tasks.await

class ProfileRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    suspend fun registerUserWithEmail(user: User, password: String, image: Uri?): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(user.email, password).await()
            registerFirestore(user, image)
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

    private suspend fun registerFirestore(user: User, image: Uri?): Result<Boolean> = try {
        val timestamp = getCurrentTimestamp()
        var currUser = user.copy(timestamp = timestamp)

        image?.let { img ->
            currUser = currUser.copy(image = postProfilePicture(user, img))
        }

        firestore.collection(USERS).document(user.email).set(currUser).await()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    private suspend fun postProfilePicture(user: User, uri: Uri): String {
        // TODO use a unique identifier for storing image as a path
        val upload = storage.reference.child("$PROFILEIMG/${user.email + "_" + user.timestamp}")
        upload.putFile(uri).await()
        return upload.downloadUrl.await().toString()
    }

    suspend fun getUser(email: String? = auth.currentUser?.email): User? {
        Log.d("email", "get user $email")
        return email?.let { user ->
            firestore.collection(USERS)
                .document(user).get().await().toObject(User::class.java)
        }
    }

    suspend fun getUserPosts(email: String? = auth.currentUser?.email): List<Post> {
        var posts = emptyList<Post>()

        if (email != null) {
            firestore.collection(USERS).document(email).collection(POSTS).get().await().forEach {
                val post = it.toObject(Post::class.java)
                posts = posts + post
            }
        }

        return posts
    }

    fun logoutUser(): Result<Boolean> = try {
        auth.signOut()
        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getBookmarks() : List<Post> {
        val user = auth.currentUser?.email
        val posts = mutableListOf<Post>()

        user?.let {
            firestore.collection(USERS).document(it).collection(BOOKMARKS).get().await().forEach { snapshot ->
                val post = snapshot.toObject(Post::class.java)
                posts.add(post)
            }
        }

        return posts
    }

    suspend fun followUser(email: String) {
        val currUser = auth.currentUser?.email
        currUser?.let { curr ->
            val follower = Follower(curr)

            firestore.collection(USERS).document(email).collection(FOLLOWERS).document(curr)
                .set(follower).await()

            try {
                val totalFollowers = firestore.collection(USERS)
                    .document(email).collection(FOLLOWERS).get().await().size()

                firestore.collection(USERS).document(email).update("followers", totalFollowers)
                    .await()
            } catch (e: Exception) {
                Log.d("Error", "Couldn't follow the user")
            }
        }
    }

    suspend fun deleteAccount() {
        val user = auth.currentUser?.email

        auth.currentUser?.delete()

        user?.let {
            firestore.collection(USERS).document(it).delete().await()
            auth.currentUser?.delete()?.await()
        }

        logoutUser()
    }
}
