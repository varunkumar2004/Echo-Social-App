package com.varunkumar.echo_social_app.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.varunkumar.echo_social_app.data.models.Comment
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.Bookmarks
import com.varunkumar.echo_social_app.utils.Constants.Companion.Comments
import com.varunkumar.echo_social_app.utils.Constants.Companion.Posts
import com.varunkumar.echo_social_app.utils.Constants.Companion.Users
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
                firestore.collection(Users).document(mail).get().await().toObject(User::class.java)

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
                        profile_image = user.image
                    )

                // TODO change to this identifier instead of only timestamp
                val identifier: String = "${email}_${currentTimestamp}"

                firestore.collection(Users).document(email)
                    .collection(Posts)
                    .document(currentTimestamp).set(post).await()

                firestore.collection(Posts).document(currentTimestamp).set(post).await()
            }
        }

        Result.Success(false)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun getPost(timestamp: String): Post? {
        val result = firestore.collection(Posts).document(timestamp).get().await()
        return result.toObject(Post::class.java)
    }

    suspend fun postComment(comment: String, post: Post): Result<Boolean> = try {
        val email = auth.currentUser?.email

        email?.let {
            val user =
                firestore.collection(Users).document(email).get().await().toObject(User::class.java)
            user?.let {
                val commentInstance = Comment(
                    comment = comment,
                    email = user.name,
                    name = user.name,
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

    suspend fun bookmarkPost(post: Post): Result<Boolean> = try {
        val user = auth.currentUser?.email

        user?.let {
            val header = post.email + "_" + post.timestamp

            firestore.collection(Users).document(it).collection(Bookmarks).document(header)
                .set(post).await()

//            firestore.collection(Users).document(post.email).collection()
        }

        Result.Success(true)
    } catch (e: Exception) {
        Result.Error(e)
    }

    suspend fun deletePost(email: String, timestamp: String) {
        // delete from user collection
        firestore.collection(Users).document(email).collection(Posts).document(timestamp).delete()
            .await()
        firestore.collection(Posts).document(timestamp).delete().await()
    }

    private suspend fun postImage(uri: Uri): String {
        // TODO add unique identifier for image name
        // identifier -> {email_timestamp}
        val upload = storage.reference.child("posts/${uri.lastPathSegment}")
        upload.putFile(uri).await()
        return upload.downloadUrl.await().toString()
    }
}