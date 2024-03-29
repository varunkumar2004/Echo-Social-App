package com.varunkumar.echo_social_app.view.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.varunkumar.echo_social_app.data.models.User
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

    var passwordVisible by remember { mutableStateOf(false) }

    var register by remember {
        mutableStateOf(true)
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
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
                                Toast
                                    .makeText(context, "Image Clicked", Toast.LENGTH_SHORT)
                                    .show()
                            }) {
                        ProfileImage(
                            isProfile = false,
                            modifier = Modifier.align(Alignment.Center),
                            size = 80.dp
                        )

                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                    }

                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            RoundTextField(
                modifier = newModifier,
                singleLine = true,
                header = "email"
            ) {
                email = it
            }

            if (register) {
                RoundTextField(
                    modifier = newModifier,
                    header = "name",
                    singleLine = true
                ) {
                    name = it
                }

                RoundTextField(
                    modifier = newModifier,
                    header = "bio",
                    singleLine = true
                ) {
                    bio = it
                }
            }

            RoundTextField(
                trailingIcon =
                if (!passwordVisible) Icons.Default.RemoveRedEye else Icons.Default.Password,
                visualTransformation =
                if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                header = "password",
                onClickTrailingIcon = {
                    passwordVisible = !passwordVisible
                }
            ) {
                password = it
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
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
                        profileViewModel.registerUser(user, password)
                    }

                    when (result) {
                        is Result.Success -> {
                            signInDone()
                        }
                        is Result.Error -> {}
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
