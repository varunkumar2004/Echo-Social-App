package com.varunkumar.echo_social_app.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.varunkumar.echo_social_app.data.models.Comment
import com.varunkumar.echo_social_app.data.models.Post
import com.varunkumar.echo_social_app.ui.theme.whitesmoke
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

    val post by postViewModel.post
    val comments by postViewModel.comments
    val modifier = Modifier.fillMaxWidth()

    LifecycleStartEffect {
        postViewModel.getPost(timestamp)

        onStopOrDispose {
            postViewModel.clearPost()
        }
    }

    // new scaffold
    Scaffold(modifier = modifier, topBar = {
        Box(contentAlignment = Alignment.CenterStart) {
            ActionBar(
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = Icons.AutoMirrored.Filled.ArrowBack,
                header = "Post",
                onClickLeadingIcon = { backToHome() }
            )
        }
    }, floatingActionButton = {
        FloatingActionButton(
            containerColor = whitesmoke,
            shape = CircleShape,
            onClick = {
                showCommentDialog = true
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }) {
        Column(
            modifier = modifier.padding(it)
        ) {
            post?.let { post ->
                postViewModel.getAllComments(post)

                ActualPost(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    post = post,
                    navController = navController,
                    isCurrUser = false,
                    onBookmarkPost = {
                        postViewModel.bookmarkPost(post)
                    }
                )
            }

            // TODO comment section
            CommentSection(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp), comments = comments
            )
        }

        if (showCommentDialog) {
            var newComment by remember {
                mutableStateOf("")
            }

            BasicAlertDialog(
                onDismissRequest = { showCommentDialog = false },
                modifier = modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = modifier
                        .padding(20.dp)

                ) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Comment") },
                        value = newComment,
                        onValueChange = { str ->
                            newComment = str
                        }
                    )

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
    isCurrUser: Boolean = true,
    modifier: Modifier,
    post: Post,
    navController: NavController,
    onDeletePost: (String) -> Unit = {},
    onBookmarkPost: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
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
                        size = 40.dp,
                        uri = post.profileImage
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = post.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(text = post.email, style = MaterialTheme.typography.bodyMedium)
                    }

                }

                IconButton(onClick = {
                    // TODO add more options
                    expanded = true
                }) {
                    Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "More Options")

                    DropdownMenu(
                        modifier = Modifier
                            .background(whitesmoke),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(text = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Bookmark")
                                Spacer(modifier = Modifier.width(20.dp))
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkAdd,
                                    contentDescription = null
                                )
                            }
                        },
                            onClick = {
                                expanded = false
                                onBookmarkPost()
                            }
                        )

                        if (!isCurrUser) {
                            DropdownMenuItem(text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Profile")
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Icon(
                                        imageVector = Icons.Outlined.Link,
                                        contentDescription = null
                                    )
                                }
                            }, onClick = {
                                navController.navigate(Routes.profile_screen.route + "/${post.email}")
                            })
                        } else {
                            DropdownMenuItem(text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Delete")
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Icon(
                                        imageVector = Icons.Outlined.DeleteForever,
                                        contentDescription = null
                                    )
                                }
                            }, onClick = {
                                // TODO delete this post
                                onDeletePost(post.timestamp)
                            })
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(5.dp))
            Text(text = post.caption, style = MaterialTheme.typography.bodyLarge)

            post.image?.let {
                // TODO downloadable url
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp)),
                        model = post.image, contentDescription = "Post Image"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                val extractTime = extractTimestamp(post.timestamp)
                Text(text = extractTime ?: "")
            }
        }
    }
}

@Composable
fun CommentSection(
    modifier: Modifier, comments: List<Comment>?
) {
    if (!comments.isNullOrEmpty()) {
        Spacer(modifier = Modifier.height(5.dp))
        LazyColumn {
            items(comments) {
                CommentPreview(
                    comment = it,
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 10.dp)
                )
            }
        }
    } else {
        Text(text = "No comments.", modifier = modifier, textAlign = TextAlign.Center)
    }
}

@Composable
fun CommentPreview(
    comment: Comment, modifier: Modifier
) {
    val radius = RoundedCornerShape(40.dp)
    Box(
        modifier = modifier
            .clip(radius)
            .border(1.dp, Color.LightGray, radius)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 20.dp)
        ) {
            // post header
            // TODO change email to name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = comment.name, style = MaterialTheme.typography.bodyLarge)
                val extractTime = extractTimestamp(comment.timestamp)
                Text(
                    text = extractTime ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }

            Text(text = comment.comment, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostPreview() {
    val comment = Comment(
        comment = "This is a great article!",
        name = "John Doe",
        user = "johndoe@example.com",
        timestamp = "20240328_220116",
        email = "johndoe@example.com"
    )

    CommentPreview(
        comment = comment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp, horizontal = 10.dp)
    )
}