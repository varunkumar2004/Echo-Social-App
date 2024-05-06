package com.varunkumar.echo_social_app.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.varunkumar.echo_social_app.data.models.Comment
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.BOOKMARKS
import com.varunkumar.echo_social_app.utils.Constants.Companion.COMMENTS
import com.varunkumar.echo_social_app.utils.Constants.Companion.POSTS
import com.varunkumar.echo_social_app.utils.Constants.Companion.USERS
import com.varunkumar.echo_social_app.utils.Result
import com.varunkumar.echo_social_app.utils.getCurrentTimestamp
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
        var image: String? = null

        email?.let { mail ->
            val user =
                firestore.collection(USERS).document(mail).get().await().toObject(User::class.java)

            uri?.let {
                image = postImage(uri)
            }

            user?.let {
                val post =
                    Post(
                        name = user.name,
                        email = user.email,
                        caption = caption,
                        timestamp = currentTimestamp,
                        image = image,
                        profileImage = user.image
                    )

                // TODO change to this identifier instead of only timestamp
//                val identifier = "${email}_${currentTimestamp}"

                firestore.collection(USERS).document(email)
                    .collection(POSTS)
                    .document(currentTimestamp).set(post).await()

                firestore.collection(POSTS).document(currentTimestamp).set(post).await()
            }
        }

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getPost(timestamp: String): Post? {
        val result = firestore.collection(POSTS).document(timestamp).get().await()
        return result.toObject(Post::class.java)
    }

    suspend fun postComment(comment: String, post: Post): Result<Boolean> = try {
        val email = auth.currentUser?.email

        email?.let {
            val user =
                firestore.collection(USERS).document(email).get().await().toObject(User::class.java)
            user?.let {
                val commentInstance = Comment(
                    comment = comment,
                    email = user.name,
                    name = user.name,
                    timestamp = post.timestamp,
                    user = post.email
                )

                firestore.collection(POSTS)
                    .document(post.timestamp)
                    .collection(COMMENTS)
                    .document(getCurrentTimestamp())
                    .set(commentInstance)
                    .await()
            }
        }

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getAllComments(post: Post): List<Comment> {
        var comments = listOf<Comment>()

        firestore.collection(POSTS)
            .document(post.timestamp)
            .collection(COMMENTS)
            .get()
            .await()
            .forEach {
                comments = comments + it.toObject(Comment::class.java)
            }

        return comments
    }

    suspend fun bookmarkPost(post: Post): Result<Boolean> = try {
        val user = auth.currentUser?.email

        user?.let {
            val header = post.email + "_" + post.timestamp

            firestore.collection(USERS).document(it).collection(BOOKMARKS).document(header)
                .set(post).await()

//            firestore.collection(Users).document(post.email).collection()
        }

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deletePost(email: String, timestamp: String) {
        // delete from user collection
        firestore.collection(USERS).document(email).collection(POSTS).document(timestamp).delete()
            .await()
        firestore.collection(POSTS).document(timestamp).delete().await()
    }

    private suspend fun postImage(uri: Uri): String {
        // TODO add unique identifier for image name
        // identifier -> {email_timestamp}
        val upload = storage.reference.child("posts/${uri.lastPathSegment}")
        upload.putFile(uri).await()
        return upload.downloadUrl.await().toString()
    }
}