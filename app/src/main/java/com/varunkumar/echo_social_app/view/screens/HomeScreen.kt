package com.varunkumar.echo_social_app.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.utils.NavItem
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.view.HomeViewModel
import com.varunkumar.echo_social_app.view.PostViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController,
    postViewModel: PostViewModel
) {
    val state by homeViewModel.state
    val posts = state.posts

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing, onRefresh = homeViewModel::getAllPosts
    )

    ModalNavigationDrawer(drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        drawerContent = {
            Column(
                modifier = Modifier.background(Color.White)
            ) {
                Text(text = "Notifications")
            }
        }) {
        Scaffold(
            bottomBar = { BottomBar(navController = navController, postViewModel = postViewModel) },
            topBar = { TopBar() },
        ) {
            Box(modifier = Modifier.padding(it)) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(posts) { post ->
                        Log.d("timestamp", post.timestamp)
                        ActualPost(
                            post = post,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp, vertical = 5.dp)
                                .clickable {
                                    navController.navigate(Routes.post_screen.route + "/${post.timestamp}")
                                },
                            navController = navController
                        )
                    }
                }

                PullRefreshIndicator(
                    refreshing = state.isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(
    navController: NavController,
    postViewModel: PostViewModel
) {
    var selected by remember {
        mutableStateOf("home")
    }

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showBottomSheet = false }
        ) {
            AddPostScreen(postViewModel = postViewModel) {
                // TODO action after posting
                showBottomSheet = false
            }
        }
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(selected = NavItem.Home.route == selected, onClick = {
            selected = NavItem.Home.route
        }, label = { Text(text = "Home") }, icon = {
            Icon(
                imageVector = NavItem.Home.icon, contentDescription = NavItem.Home.dest
            )
        })

        NavigationBarItem(selected = NavItem.AddPost.route == selected, onClick = {
//            selected = NavItem.AddPost.route
//            navController.navigate(NavItem.AddPost.dest)
            showBottomSheet = true
        }, label = { Text(text = "Post") }, icon = {
            Icon(
                imageVector = NavItem.AddPost.icon, contentDescription = NavItem.AddPost.dest
            )
        })


        NavigationBarItem(selected = NavItem.Search.route == selected, onClick = {
            selected = NavItem.Search.route
            navController.navigate(NavItem.Search.dest)
        }, label = { Text(text = "Search") }, icon = {
            Icon(
                imageVector = NavItem.Search.icon, contentDescription = NavItem.Search.dest
            )
        })

        NavigationBarItem(selected = NavItem.Profile.route == selected, onClick = {
            selected = NavItem.Profile.route
            navController.navigate(NavItem.Profile.dest + "/1")
        }, label = { Text(text = "Profile") }, icon = {
            Icon(
                imageVector = NavItem.Profile.icon, contentDescription = NavItem.Profile.dest
            )
        })
    }
}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {
        Text(text = "Echo", fontSize = 18.sp, textAlign = TextAlign.Center, fontStyle = FontStyle.Italic)
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    size: Dp
) {
    Box(
        modifier = modifier
            .size(size)
            .border(0.5.dp, Color.DarkGray, CircleShape)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {

    }
}

val post = listOf(
    Post(
        email = "RH",
        image = "",
        caption = "I saw people saying they didn't want 3 Kanye albums with Ty Dolla Sign, I literally couldn't disagree more.",
        timestamp = "2h",
        bookmarks = 123
    ),
    Post(
        email = "Formula 1",
        timestamp = "1h",
        caption = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed feugiat diam et libero ultrices accumsan. Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec a ligula ut sem tempor finibus. Nam finibus mattis dolor quis elementum. Morbi dapibus viverra ipsum eget luctus.",
        bookmarks = 345
    ),
    Post(
        email = "vkumar@gmail.com",
        timestamp = "20240207_120133",
        caption = "post result",
        image = "",
        bookmarks = 0
    ),
)

@Preview(showBackground = true)
@Composable
fun HomePreview() {
//    HomeScreen(navController = rememberNavController())
}