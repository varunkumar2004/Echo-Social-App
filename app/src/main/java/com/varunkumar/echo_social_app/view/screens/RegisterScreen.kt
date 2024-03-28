package com.varunkumar.echo_social_app.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
            verticalArrangement = Arrangement.spacedBy(5.dp),
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
                    ProfileImage(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        size = 80.dp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
            }

            OutlinedTextField(
                modifier = newModifier,
                label = { Text("email") },
                value = email,
                onValueChange = {
                    email = it
                }
            )

            if (register) {
                OutlinedTextField(
                    modifier = newModifier,
                    label = { Text("name") },
                    value = name,
                    onValueChange = {
                        name = it
                    }
                )

                OutlinedTextField(
                    modifier = newModifier,
                    label = { Text("bio") },
                    value = bio,
                    onValueChange = {
                        bio = it
                    }
                )
            }

            OutlinedTextField(
                modifier = newModifier,
                label = { Text("Password") },
                value = password,
                onValueChange = {
                    password = it
                },
                visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    val image =
                        if (!passwordVisible) Icons.Default.RemoveRedEye else Icons.Default.Password
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description)
                    }
                }
            )

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

                        is Result.Error -> {

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

@Composable
fun RoundedTextField(
    modifier: Modifier = Modifier,
    cornerSize: Dp = 40.dp,
    placeholder: String? = null
) {
    var text by remember {
        mutableStateOf("")
    }

    val radius = RoundedCornerShape(cornerSize)

    OutlinedTextField(
        modifier = modifier,
        value = text,
        onValueChange = {
            text = it
//            returnText()
        },
        placeholder = { placeholder?.let { Text(placeholder) } }
    )
}

//@Composable
//fun RegisterLogin(
//    isForRegister: Boolean = true, // true for register, false for log in
//    profileViewModel: ProfileViewModel,
//    backToButtons: () -> Unit,
//    signInDone: () -> Unit
//) {
//    var email by remember { mutableStateOf("") }
//    var name by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var bio by remember { mutableStateOf("") }
//
//    var passwordVisible by remember { mutableStateOf(true) }
//    val modifier = Modifier.fillMaxWidth()
//
//    var register by remember {
//        mutableStateOf(true)
//    }
//
//    Column(
//        verticalArrangement = Arrangement.spacedBy(5.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = modifier,
//            contentAlignment = Alignment.CenterStart
//        ) {
//            IconButton(
//                onClick = {
//                    register = !register
//                }
//            ) {
//                Text(
//                    text =
//                    if (register) "Register?"
//                    else "Login?"
//                )
//            }
//
//            if (register) {
//                ProfileImage(
//                    modifier = Modifier.align(Alignment.CenterEnd),
//                    size = 80.dp
//                )
//            }
//
//            Spacer(modifier = Modifier.height(10.dp))
//        }
//
//        OutlinedTextField(
//            modifier = modifier,
//            label = { Text("email") },
//            value = email,
//            onValueChange = {
//                email = it
//            }
//        )
//
//        if (register) {
//            OutlinedTextField(
//                modifier = modifier,
//                label = { Text("name") },
//                value = name,
//                onValueChange = {
//                    name = it
//                }
//            )
//
//            OutlinedTextField(
//                modifier = modifier,
//                label = { Text("bio") },
//                value = bio,
//                onValueChange = {
//                    bio = it
//                }
//            )
//        }
//
//        OutlinedTextField(
//            modifier = modifier,
//            label = { Text("Password") },
//            value = password,
//            onValueChange = {
//                password = it
//            },
//            visualTransformation = if (!passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
//            trailingIcon = {
//                val image =
//                    if (passwordVisible) Icons.Default.RemoveRedEye else Icons.Default.Password
//                val description = if (passwordVisible) "Hide password" else "Show password"
//
//                IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                    Icon(imageVector = image, contentDescription = description)
//                }
//            }
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        OutlinedButton(
//            modifier = modifier
//                .height(TextFieldDefaults.MinHeight),
//            onClick = {
//                if (!register) {
//                    profileViewModel.loginUser(email, password)
//                } else {
//                    val user = User(
//                        email = email,
//                        bio = bio,
//                        name = name
//                    )
//                    profileViewModel.registerUser(user, password)
//                }
//            }) {
//            Text(
//                text = if (register) "Register" else "Login"
//            )
//        }
//    }
//}

@Preview(showBackground = true)
@Composable
fun RegisterPreview() {
//    RegisterLogin(true, {})
}
