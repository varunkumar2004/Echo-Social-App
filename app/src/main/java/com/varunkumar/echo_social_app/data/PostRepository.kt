package com.varunkumar.echo_social_app.data

import android.net.Uri
import android.util.Log
import com.varunkumar.echo_social_app.data.models.Comment
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.Comments
import com.varunkumar.echo_social_app.utils.Constants.Companion.Posts
import com.varunkumar.echo_social_app.utils.Constants.Companion.Users
import com.varunkumar.echo_social_app.utils.Result
import com.varunkumar.echo_social_app.utils.getCurrentTimestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class PostRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    // post new entry in each user posts collection and global collection
    suspend fun post(caption: String, uri: Uri?): Result<Boolean> = try {
        val email = auth.currentUser?.email
        val currentTimestamp = getCurrentTimestamp()
        var image = ""

        uri?.let {
            image = postImage(uri)
        }

        val post =
            Post(email = email!!, caption = caption, timestamp = currentTimestamp, image = image)

        firestore.collection(Users).document(email)
            .collection(Posts)
            .document(currentTimestamp).set(post).await()

        firestore.collection(Posts).document(currentTimestamp).set(post).await()

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getPost(timestamp: String): Post? {
        val result = firestore.collection(Posts).document(timestamp).get().await()
        val post = result.toObject(Post::class.java)
        Log.d("get post", "$post")
        return post
    }

    suspend fun postComment(comment: String, post: Post): Result<Boolean> = try {
        val user = auth.currentUser?.email

        user?.let {
            val commentInstance = Comment(
                comment = comment,
                email = user,
                timestamp = post.timestamp,
                user = post.email
            )

            firestore.collection(Posts)
                .document(post.timestamp)
                .collection(Comments)
                .document(getCurrentTimestamp())
                .set(commentInstance)
                .await()
        }

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getAllComments(post: Post): List<Comment> {
        var comments = listOf<Comment>()

        firestore.collection(Posts)
            .document(post.timestamp)
            .collection(Comments)
            .get()
            .await()
            .forEach {
                comments = comments + it.toObject(Comment::class.java)
            }

        return comments
    }

    suspend fun bookmarkPost() {

    }

    suspend fun updatePostsCount(email: String): Result<Boolean> = try {
        val docRef = firestore.collection(Users).document(email)
        val user = docRef.get().await().toObject(User::class.java)

        user?.let {
            docRef.update("posts", user.posts + 1).await()
        }

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    private suspend fun postImage(uri: Uri): String {
        // TODO add unique identifier for image name
        val upload = storage.reference.child("posts/${uri.lastPathSegment}")
        upload.putFile(uri).await()
        return upload.downloadUrl.await().toString()
    }
}