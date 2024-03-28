package com.varunkumar.echo_social_app.view.screens

import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.utils.extractTimestamp
import com.varunkumar.echo_social_app.view.ProfileViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    email: String,
    backButton: () -> Unit,
    loggingOut: () -> Unit
) {
    // TODO restrict profile screen to edit only by correct user
    val currState by profileViewModel.currProfileState
    Log.d("curr user", currState.user.toString())

    val buttonModifier = Modifier
        .clip(CircleShape)
        .background(MaterialTheme.colorScheme.primaryContainer)
        .border(1.dp, Color.LightGray, CircleShape)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(contentAlignment = Alignment.CenterStart) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = {
                        backButton()
                    }
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to Home")
                }

                Text(
                    text = "Profile",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd)
                        .padding(end = 10.dp),
                    onClick = {
                        profileViewModel.logoutUser()
                        loggingOut()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Logout")
                }
            }
        },
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            val modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)

            if (email != "1") {
                profileViewModel.getUser(email)
                val state = profileViewModel.profileState.value
                ProfileSection(
                    navController = navController,
                    modifier = modifier,
                    user = state.user,
                    posts = state.posts,
                    postCount = state.postCount
                )
            } else {
                profileViewModel.getCurrentUser()
                Log.d("posts", currState.posts.toString())
                ProfileSection(
                    navController = navController,
                    modifier = modifier,
                    user = currState.user,
                    posts = currState.posts,
                    postCount = currState.postCount
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
    postCount: Int
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
                        size = 40.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    // TODO change email to name
                    Text(text = (user?.email) ?: "", style = MaterialTheme.typography.bodyLarge)
                }

                IconButton(onClick = {
                    // TODO edit profile functionality
                }) {
                    Icon(imageVector = Icons.Default.EditNote, contentDescription = "Edit Profile")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!user?.bio.isNullOrBlank() || !user?.bio.isNullOrEmpty()) {
                Text(text = (user?.bio) ?: "", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(15.dp))
            }

            user?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TODO correct counters for each
                    CounterRow(label = "Posts", value = postCount)
                    CounterRow(label = "Following", value = user.following)
                    CounterRow(label = "Followers", value = user.followers)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            // TODO add timestamp to user object
            val timestamp = user?.timestamp?.let { extractTimestamp(it) }
            val nationality = user?.nationality

            Row {
                Text(text = "Signed On $timestamp", fontStyle = FontStyle.Italic)
                if (nationality != null) {
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "·", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = user.nationality, fontStyle = FontStyle.Italic)
                }
            }
        }

        PostsSection(navController = navController, posts = posts)
    }
}

@Composable
fun CounterRow(label: String, value: Int) {
    Row {
        Text(text = "$label: ")
        Text(text = value.toString(), fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PostsSection(
    navController: NavController,
    posts: List<Post>
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
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
                navController = navController
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    val user = User(
        name = "John Doe",
        bio = "A passionate developer",
        email = "john@example.com",
        image = "profile.jpg",
        posts = 10,
        followers = 100,
        following = 50,
        nationality = "India"
    )
}

