package com.varunkumar.echo_social_app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object AppModule {
    fun authInstance(): FirebaseAuth {
        val result by lazy {
            FirebaseAuth.getInstance()
        }

        return result
    }

    fun firestoreInstance(): FirebaseFirestore {
        val result by lazy {
            FirebaseFirestore.getInstance()
        }

        return result
    }

    fun storageInstance(): FirebaseStorage {
        val result by lazy {
            FirebaseStorage.getInstance()
        }

        return result
    }
}
