package com.varunkumar.echo_social_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.varunkumar.echo_social_app.view.HomeViewModel
import com.varunkumar.echo_social_app.view.PostViewModel
import com.varunkumar.echo_social_app.view.ProfileViewModel
import com.varunkumar.echo_social_app.view.SearchViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val profileViewModel = viewModel<ProfileViewModel>()
            val postViewModel = viewModel<PostViewModel>()
            val searchViewModel = viewModel<SearchViewModel>()

            // updated viewModel
            val homeViewModel = viewModel<HomeViewModel>()
            Navigation(
                navController = navController,
                profileViewModel = profileViewModel,
                postViewModel = postViewModel,
                homeViewModel = homeViewModel,
                searchViewModel = searchViewModel
            )

        }
    }
}