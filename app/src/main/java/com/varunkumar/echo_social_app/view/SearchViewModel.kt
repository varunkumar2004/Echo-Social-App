package com.varunkumar.echo_social_app.view

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varunkumar.echo_social_app.AppModule
import com.varunkumar.echo_social_app.data.SearchRepository
import com.varunkumar.echo_social_app.data.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val searchRepository: SearchRepository = SearchRepository(
//        AppModule.authInstance(),
        AppModule.firestoreInstance()
    )

    private val _users = mutableStateOf<List<User>?>(null)
    val users get() = _users

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private fun searchUser() {
        viewModelScope.launch {
            _users.value = searchRepository.getAllUsers(_searchText.value)
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun onToggleSearch() {
        _isSearching.value = true
        searchUser()
        Log.d("search user", "${users.value}")
        _isSearching.value = false
    }
}