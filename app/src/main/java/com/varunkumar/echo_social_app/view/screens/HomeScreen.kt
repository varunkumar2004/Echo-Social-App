package com.varunkumar.echo_social_app.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.varunkumar.echo_social_app.ui.theme.whitesmoke
import com.varunkumar.echo_social_app.utils.NavItem
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.view.HomeViewModel
import com.varunkumar.echo_social_app.view.PostViewModel
import kotlinx.coroutines.launch

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

    val listState = rememberLazyListState()

    Scaffold(
        bottomBar = { BottomBar(navController = navController, postViewModel = postViewModel) },
        topBar = {
            ActionBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                header = "Echo"
            )
        },
    ) {
        Box(modifier = Modifier.padding(it)) {
            LazyColumn(
                state = listState,
                reverseLayout = true,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(posts) { post ->
                    Log.d("timestamp", post.timestamp)
                    ActualPost(
                        isCurrUser = false,
                        post = post,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 2.dp)
                            .clickable {
                                navController.navigate(Routes.post_screen.route + "/${post.timestamp}")
                            },
                        navController = navController
                    )
                }
            }

            LaunchedEffect(posts.size) {
                if (posts.isNotEmpty()) {
                    listState.animateScrollToItem(posts.size - 1)
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
                scope.launch {
                    sheetState.hide()
                }.invokeOnCompletion {
                    showBottomSheet = false
                }
            }
        }
    }

    NavigationBar(
        containerColor = whitesmoke,
        modifier = Modifier
            .fillMaxWidth()
            .height(TextFieldDefaults.MinHeight + 10.dp),
    ) {
        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.LightGray
            ),
            selected = NavItem.Home.route == selected,
            onClick = {
                selected = NavItem.Home.route
            },
            alwaysShowLabel = false,
            icon = {
                Icon(
                    imageVector = NavItem.Home.icon, contentDescription = NavItem.Home.dest
                )
            },
        )

        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.LightGray
            ),
            selected = NavItem.AddPost.route == selected,
            onClick = {
                showBottomSheet = true
            }, alwaysShowLabel = false, icon = {
                Icon(
                    imageVector = NavItem.AddPost.icon, contentDescription = NavItem.AddPost.dest
                )
            }
        )


        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.LightGray
            ),
            selected = NavItem.Search.route == selected,
            onClick = {
                selected = NavItem.Search.route
                navController.navigate(NavItem.Search.dest)
            }, alwaysShowLabel = false, icon = {
                Icon(
                    imageVector = NavItem.Search.icon, contentDescription = NavItem.Search.dest
                )
            }
        )

        NavigationBarItem(
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.LightGray
            ),
            selected = NavItem.Profile.route == selected,
            onClick = {
                selected = NavItem.Profile.route
                navController.navigate(NavItem.Profile.dest + "/1")
            }, alwaysShowLabel = false, icon = {
                Icon(
                    imageVector = NavItem.Profile.icon, contentDescription = NavItem.Profile.dest
                )
            }
        )
    }
}

@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    isProfile: Boolean = true,
    size: Dp,
    uri: String? = null
) {
    val newmodifier = Modifier.fillMaxWidth()

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {
        if (uri == null) {
            if (isProfile) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = newmodifier
                        .align(Alignment.Center)
                )
            }
        } else {
            AsyncImage(
                modifier = newmodifier
                    .align(Alignment.Center),
                model = uri,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
//    HomeScreen(navController = rememberNavController())
}