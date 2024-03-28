package com.varunkumar.echo_social_app.data

import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Constants.Companion.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SearchRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    suspend fun searchUser(user: String): User? {
        val result = firestore.collection(Users).document(user).get().await()
        return result.toObject(User::class.java)
    }

//    suspend fun getUsers(): List<User> {
//        var users = emptyList<User>()
//
//        // TODO only for databases
//        firestore.collection(Users).get().await().forEach {
//            users = users + it.toObject(User::class.java)
//        }
//
//        return users
//    }
}