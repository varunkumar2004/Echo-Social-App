package com.varunkumar.echo_social_app.view.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.varunkumar.echo_social_app.view.PostViewModel

@Composable
fun AddPostScreen(
    postViewModel: PostViewModel,
    onPostDone: () -> Unit
) {
    var caption by remember {
        mutableStateOf("")
    }

    var selected by remember {
        mutableStateOf(false)
    }

    var selectedImage by remember {
        mutableStateOf<Uri?>(Uri.EMPTY)
    }

    val context = LocalContext.current

    val singlePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImage = uri
            selected = true
        }
    )

    fun selectedPhoto() {
        singlePhotoPicker
            .launch(
                PickVisualMediaRequest
                    (ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
    }

    val modifier = Modifier
        .fillMaxWidth()

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "New Post",
            modifier = modifier,
            textAlign = TextAlign.Center
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = modifier,
                value = caption,
                onValueChange = {
                    caption = it
                },
                label = { Text("caption...") },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (caption.isNotBlank() || caption.isNotEmpty()) {
                                postViewModel.post(
                                    caption = caption,
                                    uri = selectedImage
                                )
                                onPostDone()
                            } else {
                                Toast.makeText(context, "Please Enter Caption", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.PostAdd, contentDescription = "Post")
                    }
                }
            )
        }

        Box(
            modifier = modifier
                .aspectRatio(1f)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.LightGray)
        ) {
            if (!selected) {
                Box(
                    modifier = modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.DarkGray)
                        .clickable {
                            selectedPhoto()
                        }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier
                            .padding(20.dp)
                    ) {
                        Text(text = "Add Image", color = Color.White)

                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "add image",
                            tint = Color.White
                        )
                    }
                }
            } else {
                IconButton(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        // TODO add animations for closing the image
                        selected = false
                        selectedImage = Uri.EMPTY
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Remove Image"
                    )
                }

                AsyncImage(
                    model = selectedImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun AddPostPreview() {
}
