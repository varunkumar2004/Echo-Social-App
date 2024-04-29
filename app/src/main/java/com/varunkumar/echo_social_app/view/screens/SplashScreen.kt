package com.varunkumar.echo_social_app.view.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.varunkumar.echo_social_app.R
import com.varunkumar.echo_social_app.ui.theme.whitesmoke
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    alreadyLoggedIn: () -> Unit
) {
    val scale = remember {
        Animatable(0f)
    }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 0.3f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        alreadyLoggedIn()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(whitesmoke),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.echo_icon),
            contentDescription = null
        )

        Text(text = "Made by Varun")
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.echo_icon),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}