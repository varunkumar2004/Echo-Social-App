package com.varunkumar.echo_social_app.view

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varunkumar.echo_social_app.AppModule
import com.varunkumar.echo_social_app.data.ProfileRepository
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Result
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val profileRepository = ProfileRepository(
        AppModule.authInstance(),
        AppModule.firestoreInstance(),
        AppModule.storageInstance()
    )

    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult get() = _authResult

    private val _currProfileState = mutableStateOf(ProfileState())
    val currProfileState get() = _currProfileState


    private val _profileState = mutableStateOf(ProfileState())
    val profileState get() = _profileState

    init {
        getCurrentUser()
    }

    fun registerUser(user: User, password: String, image: Uri?) {
        viewModelScope.launch {
            _authResult.value = profileRepository.registerUserWithEmail(user, password, image)
            Log.d("image", "image selected $image")
        }
        loginUser(user.email, password)
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = profileRepository.loginUserWithEmail(email, password)
        }
    }

    fun getUser(email: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(
                user = profileRepository.getUser(email)
            )

            getUserPosts(email)
        }
    }

    private fun getUserPosts(email: String) {
        viewModelScope.launch {
            val posts = profileRepository.getUserPosts(email)
            _profileState.value = _profileState.value.copy(
                posts = posts, postCount = posts.size
            )
        }
    }

    private fun getPosts(email: String) {
        viewModelScope.launch {
            val posts = profileRepository.getUserPosts(email)
            _currProfileState.value = _currProfileState.value.copy(
                posts = posts, postCount = posts.size
            )
        }
    }

    fun getCurrentUser() {
        viewModelScope.launch {
            val user = profileRepository.getCurrentUser()
            user?.let { getPosts(user.email) }
            _currProfileState.value = _currProfileState.value.copy(
                user = user
            )
        }
    }

    fun logoutUser() {
        _authResult.value = profileRepository.logoutUser()
        _currProfileState.value = ProfileState()
    }

    fun followUser(email: String) {
        viewModelScope.launch {
            profileRepository.followUser(email)
        }
    }
}

data class ProfileState(
    val user: User? = null,
    val posts: List<Post> = emptyList(),
    val postCount: Int = 0
)