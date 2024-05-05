package com.varunkumar.echo_social_app.view.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.ui.theme.whitesmoke
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.utils.TabItem
import com.varunkumar.echo_social_app.utils.extractTimestamp
import com.varunkumar.echo_social_app.view.PostViewModel
import com.varunkumar.echo_social_app.view.ProfileState
import com.varunkumar.echo_social_app.view.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    postViewModel: PostViewModel,
    profileViewModel: ProfileViewModel,
    email: String,
    backButton: () -> Unit,
    loggingOut: () -> Unit
) {
    // TODO restrict profile screen to edit only by correct user
    profileViewModel.toggleFollow(email)
    val currState by profileViewModel.currProfileState
    val trailingIcon = if (email == "1") Icons.Default.Logout else null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ActionBar(
                leadingIcon = Icons.Default.ArrowBack,
                header = "Profile",
                trailingIcon = trailingIcon,
                onClickLeadingIcon = { backButton() },
                onClickTrailingIcon = {
                    profileViewModel.logoutUser()
                    loggingOut()
                }
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)

            if (email != "1") {
                profileViewModel.getUser(email)
                val state = profileViewModel.profileState.value

                ProfileSection(
                    viewModel = profileViewModel,
                    navController = navController,
                    modifier = modifier,
                    state = state,
                    showFollowButton = profileViewModel.showFollow.value,
                    onFollowClick = {
                        profileViewModel.followUser(email)
                    }
                )
            } else {
                profileViewModel.getUser()

                ProfileSection(
                    viewModel = profileViewModel,
                    navController = navController,
                    modifier = modifier,
                    state = currState,
                    showFollowButton = profileViewModel.showFollow.value,
                    onDeletePost = { timestamp ->
                        currState.user?.let { user ->
                            postViewModel.deletePost(user.email, timestamp = timestamp)
                            profileViewModel.getUser()
                        }
                    },
                    onDeleteAccount = {
                        profileViewModel.deleteAccount()
                        loggingOut()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSection(
    state: ProfileState,
    viewModel: ProfileViewModel,
    navController: NavController,
    modifier: Modifier,
    showFollowButton: Boolean,
    onFollowClick: () -> Unit = {},
    onDeletePost: (String) -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    val selectedTab by viewModel.selectedTabIndex
    var posts: List<Post>?
    var showDialog by remember {
        mutableStateOf(false)
    }

    val tabItems = if (!showFollowButton) {
        mutableListOf(
            TabItem("Posts"),
            TabItem("Bookmarks")
        )
    } else {
        mutableListOf(
            TabItem("Posts")
        )
    }

    Column {
        Column(
            modifier = modifier
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileImage(
                        size = 40.dp,
                        uri = state.user?.image
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        state.user?.let { user ->
                            Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(text = user.email, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                if (showFollowButton) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = whitesmoke,
                            contentColor = Color.Black
                        ),
                        onClick = {
                            onFollowClick()
                        }
                    ) {
                        Text(text = "Follow")
                    }
                }

                if (!showFollowButton) {
                    IconButton(
                        onClick = { showDialog = true },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = whitesmoke,
                            contentColor = Color.Red
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Delete Account"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!state.user?.bio.isNullOrBlank() || !state.user?.bio.isNullOrEmpty()) {
                Text(
                    text = (state.user?.bio) ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
            }


            val timestamp = state.user?.timestamp?.let { extractTimestamp(it) }

            timestamp?.let { time ->
                Row {
                    Text(
                        text = "Signed On $time",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            state.user?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TODO correct counters for each
                    CounterRow(label = "Posts", value = state.posts.size)
                    CounterRow(label = "Followers", value = state.user.followers)
                }
            }
        }

        TabRow(selectedTabIndex = selectedTab) {
            tabItems.forEachIndexed { index, item ->
                Tab(
                    selectedContentColor = Color.Black,
                    unselectedContentColor = Color.DarkGray,
                    selected = index == selectedTab,
                    onClick = {
                        viewModel.selectTab(index)
                    },
                    text = { Text(text = item.title) }
                )
            }
        }

        if (showDialog) {
            AlertDialog(onDismissRequest = { showDialog = false }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(whitesmoke)
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Are you sure you want to delete this account? All data associated with this account will be lost permanently.",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text(text = "Cancel")
                        }

                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ),
                            onClick = {
                                // TODO delete account
                                onDeleteAccount()
                                showDialog = false
                            }
                        ) {
                            Text(text = "Delete Account")
                        }
                    }
                }
            }
        }

        posts = if (selectedTab == 0) {
            state.posts
        } else {
            state.bookmarks
        }

        PostsSection(
            navController = navController,
            posts = posts,
            isCurrUser = !showFollowButton,
            onDeletePost = {
                onDeletePost(it)
            }
        )
    }
}

@Composable
fun CounterRow(label: String, value: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Text(text = value.toString(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PostsSection(
    navController: NavController,
    posts: List<Post>?,
    isCurrUser: Boolean,
    onDeletePost: (String) -> Unit
) {
    val listState = rememberLazyListState()

    Log.d("posts", posts.toString())

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        reverseLayout = true
    ) {
        posts?.let {

            items(posts) { post ->
                ActualPost(
                    post = post,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 15.dp)
                        .clickable {
                            navController.navigate(Routes.post_screen.route + "/${post.timestamp}")
                        },
                    navController = navController,
                    isCurrUser = isCurrUser,
                    onDeletePost = {
                        onDeletePost(post.timestamp)
                    }
                )
            }
        }
    }

    LaunchedEffect(posts?.size) {
        if (posts?.isNotEmpty() == true) {
            listState.animateScrollToItem(posts.size - 1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {

}

