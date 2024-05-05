package com.varunkumar.echo_social_app.view.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.ui.theme.whitesmoke
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.utils.extractTimestamp
import com.varunkumar.echo_social_app.view.PostViewModel
import com.varunkumar.echo_social_app.view.ProfileViewModel

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
    profileViewModel.getUser()
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
//                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                .padding(horizontal = 20.dp)

            if (email != "1") {
                profileViewModel.getUser(email)

                val state = profileViewModel.profileState.value
                ProfileSection(
                    navController = navController,
                    modifier = modifier,
                    user = state.user,
                    posts = state.posts,
                    postCount = state.postCount,
                    showFollowButton = profileViewModel.showFollow.value,
                    onFollowClick = {
                        profileViewModel.followUser(email)
                    }
                )
            } else {
                ProfileSection(
                    navController = navController,
                    modifier = modifier,
                    user = currState.user,
                    posts = currState.posts,
                    postCount = currState.postCount,
                    showFollowButton = profileViewModel.showFollow.value,
                    onDeletePost = { timestamp ->
                        currState.user?.let { user ->
                            postViewModel.deletePost(user.email, timestamp = timestamp)
                            profileViewModel.getUser()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileSection(
    navController: NavController,
    modifier: Modifier,
    user: User?,
    posts: List<Post>,
    postCount: Int,
    showFollowButton: Boolean,
    onFollowClick: () -> Unit = {},
    onDeletePost: (String) -> Unit = {}
) {
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
                        uri = user?.image
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        user?.let {
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
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!user?.bio.isNullOrBlank() || !user?.bio.isNullOrEmpty()) {
                Text(
                    text = (user?.bio) ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(10.dp))
            }


            val timestamp = user?.timestamp?.let { extractTimestamp(it) }

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

            user?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TODO correct counters for each
                    CounterRow(label = "Posts", value = posts.size)
                    CounterRow(label = "Followers", value = user.followers)
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
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
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value.toString(), fontWeight = FontWeight.Bold)
        Text(text = label)
    }
}

@Composable
fun PostsSection(
    navController: NavController,
    posts: List<Post>,
    isCurrUser: Boolean,
    onDeletePost: (String) -> Unit
) {
    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        reverseLayout = true
    ) {
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

    LaunchedEffect(posts.size) {
        if (posts.isNotEmpty()) {
            listState.animateScrollToItem(posts.size - 1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {

}

