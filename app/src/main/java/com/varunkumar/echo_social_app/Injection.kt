package com.varunkumar.echo_social_app

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

object Injection {
    private val instance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun instance(): FirebaseFirestore {
        return instance
    }

    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    fun storage(): FirebaseStorage {
        return storage
    }
}