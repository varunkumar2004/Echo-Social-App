package com.varunkumar.echo_social_app.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varunkumar.echo_social_app.Injection
import com.varunkumar.echo_social_app.data.UserRepository
import com.varunkumar.echo_social_app.data.models.Post
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state get() = _state

    private val userRepository =
        UserRepository(FirebaseAuth.getInstance(), Injection.instance())

    private var _posts = MutableLiveData<List<Post>>()
    val posts get() = _posts

    init {
        getAllPosts()
    }

    fun getAllPosts() {
        // TODO get refreshing logic right
        _state.value = _state.value.copy(isRefreshing = true)

        viewModelScope.launch {
            _state.value = _state.value.copy(posts = userRepository.getAllPosts())
        }.invokeOnCompletion {
            _state.value = _state.value.copy(isRefreshing = false)
        }
    }
}

data class HomeState(
    val isRefreshing: Boolean = false,
    var posts: List<Post> = emptyList()
)