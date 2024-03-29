package com.varunkumar.echo_social_app.view.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.view.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    backButton: () -> Unit
) {
    val searchText by searchViewModel.searchText.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()
    val user by searchViewModel.user.collectAsState(null)

    Scaffold(
        topBar = {
            ActionBar(
                leadingIcon = Icons.Default.ArrowBack,
                header = "Search",
                onClickLeadingIcon = {
                    backButton()
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            RoundTextField(
                header = null,
                singleLine = true,
                trailingIcon = Icons.Default.Search,
                onClickTrailingIcon = {
                }
            ) { str ->
                // TODO search
            }

            if (isSearching) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    if (user != null) {
                        SearchItem(
                            user = user!!,
                            modifier = Modifier.fillMaxWidth(),
                        ) {

                        }
                    } else {
                        Text(text = "no such user exists", textAlign = TextAlign.Center)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Search History")
                    IconButton(onClick = {
                        // TODO clear cache of all stored searches
                        searches = emptyList()
                    }) {
                        Icon(imageVector = Icons.Default.ClearAll, contentDescription = "clear all")
                    }
                }

                LazyColumn {
                    items(searches) {user ->
                        SearchItem(
                            user = user,
                            modifier = Modifier.fillMaxWidth(),
                            buffered = true
                        ) {

                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
//    SearchItem(user = searches[0], modifier = Modifier.fillMaxWidth())
}

@Composable
fun SearchItem(
    user: User,
    modifier: Modifier,
    buffered: Boolean = false,
    onClick: () -> Unit
) {
    val radius = RoundedCornerShape(40.dp)
    Box(
        modifier = modifier
            .clip(radius)
            .border(1.dp, Color.LightGray, radius)
            .clickable { onClick() }
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = 20.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!buffered) {
                    ProfileImage(size = 40.dp)
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(text = user.email)
            }

            if (buffered) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null
                )
            }
        }
    }
}

var searches = listOf(
    User(
        name = "John Doe",
        bio = "I love coding!",
        email = "john@example.com",
        image = "profile.jpg",
        followers = 100,
        following = 50
    ),
    User(
        name = "Alice Smith",
        bio = "Exploring the world.",
        email = "alice@example.com",
        image = "avatar.png",
        followers = 200,
        following = 150
    )
)