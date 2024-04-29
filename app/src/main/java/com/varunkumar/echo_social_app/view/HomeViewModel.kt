package com.varunkumar.echo_social_app.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varunkumar.echo_social_app.AppModule
import com.varunkumar.echo_social_app.data.PostRepository
import com.varunkumar.echo_social_app.data.UserRepository
import com.varunkumar.echo_social_app.data.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel  : ViewModel() {
    private val userRepository = UserRepository(
        AppModule.authInstance(),
        AppModule.firestoreInstance()
    )

    private val _state = mutableStateOf(HomeState())
    val state get() = _state

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