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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.view.SearchViewModel

@Composable
fun SearchScreen(
    navController: NavController,
    searchViewModel: SearchViewModel,
    backButton: () -> Unit
) {
    val searchText by searchViewModel.searchText.collectAsState()
    val isSearching by searchViewModel.isSearching.collectAsState()
    val users by searchViewModel.users

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
                .padding(vertical = 0.dp, horizontal = 20.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText,
                label = { Text(text = "Search") },
                onValueChange = searchViewModel::onSearchTextChange,
                trailingIcon = {
                    IconButton(onClick = searchViewModel::onToggleSearch) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            )

            users?.let { res ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    items(res) { user ->
                        SearchItem(user = user, modifier = Modifier.fillMaxWidth()) {
                            navController.navigate(Routes.profile_screen.route + "/${user.email}")
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
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(size = 40.dp)
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = user.name)
                    Text(text = user.email)
                }
            }
        }
    }
}