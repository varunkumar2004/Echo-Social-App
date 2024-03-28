package com.varunkumar.echo_social_app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.varunkumar.echo_social_app.Injection
import com.varunkumar.echo_social_app.data.SearchRepository
import com.varunkumar.echo_social_app.data.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel : ViewModel() {
    private val searchRepository =
        SearchRepository(FirebaseAuth.getInstance(), Injection.instance())

    private val _user = MutableStateFlow<User?>(null)
    val user get() = _user.asStateFlow().debounce(500L)

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private fun searchUser() {
        viewModelScope.launch {
            _user.value = searchRepository.searchUser(searchText.value)
        }
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        searchUser()
    }

    fun onToggleSearch() {
        _isSearching.value = !_isSearching.value
        if (!_isSearching.value) {
            onSearchTextChange("")
        }
    }
}