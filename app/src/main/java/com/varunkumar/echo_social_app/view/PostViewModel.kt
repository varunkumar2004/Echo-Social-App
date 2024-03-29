package com.varunkumar.echo_social_app.view

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varunkumar.echo_social_app.Injection
import com.varunkumar.echo_social_app.data.PostRepository
import com.varunkumar.echo_social_app.data.models.Comment
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.utils.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {
    private val postRepository = PostRepository(FirebaseAuth.getInstance(), Injection.instance(), Injection.storage())

    private val _result = MutableLiveData<Result<Boolean>>()
    val result get() = _result

    private val _comments = MutableLiveData<List<Comment>>()
    val comments get() = _comments

    private val _state = mutableStateOf<Post?>(null)
    val state by _state

    private val _post = MutableLiveData<Post?>()
    val post get() = _post

    fun post(uri: Uri? = null, caption: String) {
        viewModelScope.launch {
            _result.value = postRepository.post(caption, uri = uri)
        }
    }

    fun getPost(timestamp: String) {
        viewModelScope.launch {
            _post.value = postRepository.getPost(timestamp)
        }
    }

    fun deletePost(email:String, timestamp: String) {
        Log.d("delete post", "$email _ $timestamp")
        viewModelScope.launch {
            postRepository.deletePost(email, timestamp)
        }
    }

    fun postComment(comment: String, post: Post) {
        viewModelScope.launch {
            postRepository.postComment(comment, post)
        }

        getAllComments(post)
    }

//    fun postImage(uri: Uri) {
//        viewModelScope.launch {
//            postRepository.postImage(uri)
//        }
//    }

    fun getAllComments(post: Post) {
        viewModelScope.launch {
            _comments.value = postRepository.getAllComments(post)
        }
    }
}