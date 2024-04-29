package com.varunkumar.echo_social_app.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.Users
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SearchRepository (
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun searchUser(email: String): User? {
        val user = firestore.collection(Users).document(email).get().await()
        return user.toObject(User::class.java)
    }

    suspend fun getAllUsers(name: String): List<User> {
        val users = mutableListOf<User>()
        val result = firestore.collection(Users).get().await()

        for (document in result) {
            val user = document.toObject(User::class.java)
            if (user.name.contains(name, ignoreCase = true) || user.email.contains(name, ignoreCase = true)) {
                users.add(user)
            }
        }

        return users
//        return users.filter {user ->
//            user.name.contains(name, ignoreCase = true) || user.email.contains(name, ignoreCase = true)
//        }
    }
}