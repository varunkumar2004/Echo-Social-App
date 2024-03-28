package com.varunkumar.echo_social_app.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.varunkumar.echo_social_app.data.models.Comment
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.utils.extractTimestamp
import com.varunkumar.echo_social_app.view.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postViewModel: PostViewModel,
    timestamp: String,
    backToHome: () -> Unit,
    navController: NavController
) {
    var showCommentDialog by remember {
        mutableStateOf(false)
    }

    val post by postViewModel.post.observeAsState()
    val comments by postViewModel.comments.observeAsState()
    val modifier = Modifier.fillMaxWidth()

    // new scaffold
    Scaffold(modifier = modifier, topBar = {
        Box(contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = {
                backToHome()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back to Home")
            }

            Text(
                text = "Echo", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center
            )
        }
    }, floatingActionButton = {
        val buttonModifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(1.dp, Color.LightGray, CircleShape)

        Column {
            IconButton(
                onClick = {
                    // TODO add comment functionality
                    showCommentDialog = true
                }, modifier = buttonModifier
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }

            Spacer(modifier = Modifier.height(10.dp))

            IconButton(
                onClick = {
                    // TODO bookmark functionality
                }, modifier = buttonModifier
            ) {
                Icon(imageVector = Icons.Default.BookmarkAdded, contentDescription = null)
            }
        }
    }) {
        Column(
            modifier = modifier.padding(it)
        ) {
            postViewModel.getPost(timestamp)

            post?.let { post ->
                postViewModel.getAllComments(post)

                ActualPost(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    post = post,
                    navController = navController
                )
            }

            // TODO comment section
            CommentSection(
                modifier = modifier.padding(10.dp), comments = comments
            )
        }

        if (showCommentDialog) {
            var newComment by remember {
                mutableStateOf("")
            }

            AlertDialog(
                onDismissRequest = { showCommentDialog = false }, modifier = modifier.padding(20.dp)
            ) {
                Column(
                    modifier = modifier
                        .padding(20.dp)
                        .background(Color.White)
                ) {
                    OutlinedTextField(value = newComment, onValueChange = {
                        newComment = it
                    }, label = { Text("New comment") })

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = modifier,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = {
                            newComment = ""
                            showCommentDialog = false
                        }) {
                            Text(text = "Cancel")
                        }

                        OutlinedButton(onClick = {
                            if (newComment.isNotBlank() || newComment.isNotEmpty()) {
                                postViewModel.postComment(newComment, post!!)
                                showCommentDialog = false
                            }
                        }) {
                            Text(text = "Send")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActualPost(
    modifier: Modifier, post: Post, navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }
    val whitesmoke = Color(0xFFF5F5F5)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(whitesmoke)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            // post header
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
                    Text(text = post.email, style = MaterialTheme.typography.bodyLarge)
                }

                IconButton(onClick = {
                    // TODO add more options
                    expanded = true
                }) {
                    Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "More Options")

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Go To Profile")
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Default.Link,
                                    contentDescription = null
                                )
                            }
                        }, onClick = {
                            navController.navigate(Routes.profile_screen.route + "/${post.email}")
                        })
                        DropdownMenuItem(text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Bookmark")
                                Spacer(modifier = Modifier.width(10.dp))
                                Icon(
                                    imageVector = Icons.Default.Bookmark,
                                    contentDescription = null
                                )
                            }
                        }, onClick = {
                            // TODO bookmark this post
                        }
                        )
                    }
                }

            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = post.caption, style = MaterialTheme.typography.bodyLarge)

            post.image?.let {
                // TODO downloadable url
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(vertical = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.DarkGray),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(model = post.image, contentDescription = "Post Image")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val extractTime = extractTimestamp(post.timestamp)
                Text(text = extractTime ?: "", fontStyle = FontStyle.Italic)
                Text(text = "·", fontWeight = FontWeight.Bold)

                Text(text = "Bookmarks: ${post.bookmarks}", fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
fun CommentSection(
    modifier: Modifier, comments: List<Comment>?
) {
    LazyColumn {
        comments?.let {
            items(comments) {
                CommentPreview(
                    comment = it, modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }
        }
    }
}

@Composable
fun CommentPreview(
    comment: Comment, modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        // post header
        // TODO change email to name
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = comment.email, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(10.dp))
            val extractTime = extractTimestamp(comment.timestamp)
            Text(text = extractTime ?: "", fontStyle = FontStyle.Italic)
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(text = comment.comment, style = MaterialTheme.typography.bodyLarge)
    }

    Divider()
}

//@Preview(showBackground = true)
//@Composable
//fun PostPreview() {
////    ActualPost(modifier = Modifier, post = Post(), navController = rememberNavController())
//}