package com.varunkumar.echo_social_app.view.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.varunkumar.echo_social_app.data.models.User
import com.varunkumar.echo_social_app.ui.theme.whitesmoke
import com.varunkumar.echo_social_app.utils.Result
import com.varunkumar.echo_social_app.view.ProfileViewModel

@Composable
fun RegisterScreen(
    profileViewModel: ProfileViewModel,
    signInDone: () -> Unit,
    modifier: Modifier
) {
    // TODO add horizontally animation
    val context = LocalContext.current
    val result by profileViewModel.authResult.observeAsState()
    val newModifier = Modifier.fillMaxWidth()
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    val maxLength = 250
    var image by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            image = it
        }

    var passwordVisible by remember { mutableStateOf(false) }

    var register by remember {
        mutableStateOf(true)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = newModifier,
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    modifier = Modifier.clickable { register = !register },
                    text =
                    if (!register) "Register?"
                    else "Login?"
                )

                if (register) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    ) {
                        ProfileImage(
                            isProfile = false,
                            modifier = Modifier.align(Alignment.Center),
                            size = 80.dp,
                            uri = image
                        )
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            TextField(
                modifier = newModifier,
                value = email,
                singleLine = true,
                label = { Text(text = "Email") },
                onValueChange = {
                    email = it
                }
            )

            if (register) {
                TextField(
                    modifier = newModifier,
                    value = name,
                    label = { Text(text = "Name") },
                    onValueChange = { name = it },
                    singleLine = true
                )

                TextField(
                    modifier = newModifier,
                    value = bio,
                    label = { Text(text = "Bio") },
                    onValueChange = {
                        if (it.length <= maxLength) {
                            bio = it
                        }
                    },
                    trailingIcon = {
                        Text(
                            text = "${maxLength - bio.length}",
                            color = Color.DarkGray,
                            fontSize = 12.sp
                        )
                    }
                )
            }

            TextField(
                modifier = newModifier,
                value = password,
                label = { Text(text = "Password") },
                onValueChange = { password = it },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector =
                            if (!passwordVisible) Icons.Default.RemoveRedEye else Icons.Default.Password,
                            contentDescription = null
                        )
                    }
                },
                visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            )

            OutlinedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = whitesmoke,
                    contentColor = Color.Black
                ),
                modifier = newModifier
                    .height(TextFieldDefaults.MinHeight),
                onClick = {
                    if (!register) {
                        profileViewModel.loginUser(email, password)
                    } else {
                        val user = User(
                            email = email,
                            bio = bio,
                            name = name
                        )
                        profileViewModel.registerUser(user, password, image)
                    }

                    when (result) {
                        is Result.Success -> {
                            signInDone()
                        }

                        is Result.Error -> {
                            Toast.makeText(
                                context,
                                "${(result as Result.Error).exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {}
                    }
                }) {
                Text(
                    text = if (register) "Register" else "Login"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
}
