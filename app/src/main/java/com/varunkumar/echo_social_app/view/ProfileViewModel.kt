package com.varunkumar.echo_social_app.view

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
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

    var showFollow = mutableStateOf(true)
        private set

    var selectedTabIndex = mutableIntStateOf(0)
        private set

    init {
        getUser()
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

    fun getUser(email: String? = null) {
        viewModelScope.launch {
            if (email != null) {
                _profileState.value = _profileState.value.copy(
                    user = profileRepository.getUser(email)
                )

                getUserPosts(email)
            } else {
                _currProfileState.value = _currProfileState.value.copy(
                    user = profileRepository.getUser()
                )

                getUserPosts()
                getBookmarks()
            }
        }
    }

    private fun getUserPosts(email: String? = null) {
        viewModelScope.launch {
            if (email != null) {
                val posts = profileRepository.getUserPosts(email)
                _profileState.value = _profileState.value.copy(
                    posts = posts
                )
            } else {
                _currProfileState.value = _currProfileState.value.copy(
                    user = profileRepository.getUser(),
                    posts = profileRepository.getUserPosts(),
                )
            }
        }
    }

    fun logoutUser() {
        _currProfileState.value = ProfileState()
        _profileState.value = ProfileState()
        _authResult.value = profileRepository.logoutUser()
    }

    fun toggleFollow(email: String) {
        showFollow.value = !(email == "1" || email == _currProfileState.value.user?.email)
    }

    fun selectTab(index: Int) {
        selectedTabIndex.intValue = index
    }

    // unfollow user function
    fun followUser(email: String) {
        viewModelScope.launch {
            profileRepository.followUser(email)
        }
    }

    fun getBookmarks() {
        viewModelScope.launch {
            val bookmarks = profileRepository.getBookmarks()

            _currProfileState.value = _currProfileState.value.copy(
                bookmarks = bookmarks
            )

            Log.d("bookmarks", bookmarks.toString())
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            profileRepository.deleteAccount()
            _currProfileState.value = ProfileState()
            _profileState.value = ProfileState()
        }
    }
}

data class ProfileState(
    val user: User? = null,
    val posts: List<Post> = emptyList(),
    val bookmarks: List<Post>? = null
)