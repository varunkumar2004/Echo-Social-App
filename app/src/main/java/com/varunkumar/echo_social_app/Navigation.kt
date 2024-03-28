package com.varunkumar.echo_social_app

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.varunkumar.echo_social_app.utils.Routes
import com.varunkumar.echo_social_app.view.HomeViewModel
import com.varunkumar.echo_social_app.view.PostViewModel
import com.varunkumar.echo_social_app.view.ProfileViewModel
import com.varunkumar.echo_social_app.view.SearchViewModel
import com.varunkumar.echo_social_app.view.screens.AddPostScreen
import com.varunkumar.echo_social_app.view.screens.HomeScreen
import com.varunkumar.echo_social_app.view.screens.PostScreen
import com.varunkumar.echo_social_app.view.screens.ProfileScreen
import com.varunkumar.echo_social_app.view.screens.RegisterScreen
import com.varunkumar.echo_social_app.view.screens.SearchScreen
import com.varunkumar.echo_social_app.view.screens.SplashScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    postViewModel: PostViewModel,
    searchViewModel: SearchViewModel
) {
    NavHost(navController = navController, startDestination = Routes.splash_screen.route) {
        val user = FirebaseAuth.getInstance().currentUser
        composable(route = Routes.splash_screen.route) {
            SplashScreen(
                alreadyLoggedIn = {
                    if (user != null) {
                        Log.d("user", "$user")
                        navController.navigate(Routes.home_screen.route)
                    } else {
                        Log.d("user", "$user")
                        navController.navigate(Routes.register_screen.route)
                    }
                }
            )
        }

        composable(route = Routes.home_screen.route) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel,
                postViewModel = postViewModel
            )
        }

        composable(route = Routes.register_screen.route) {
            RegisterScreen(
                profileViewModel = profileViewModel,
                signInDone = { navController.navigate(Routes.home_screen.route) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 40.dp)
            )
        }

        // this is the post screen on click
        composable(
            route = Routes.post_screen.route + "/{timestamp}",
            arguments = listOf(
                navArgument("timestamp") { type = NavType.StringType }
            )
        ) {
            PostScreen(
                navController = navController,
                postViewModel = postViewModel,
                timestamp = it.arguments?.getString("timestamp") ?: "",
                backToHome = {
                    navController.navigateUp()
                }
            )
        }


        composable(route = Routes.search_screen.route) {
            SearchScreen(searchViewModel = searchViewModel) {
                navController.navigateUp()
            }
        }

        composable(route = Routes.add_post_screen.route) {
            AddPostScreen(postViewModel = postViewModel) {
                navController.navigate(Routes.home_screen.route)
            }
        }

        composable(
            route = Routes.profile_screen.route + "/{email}",
            arguments = listOf(
                navArgument("email") {
                    type = NavType.StringType
                }
            )
        ) {
            ProfileScreen(
                navController = navController,
                profileViewModel = profileViewModel,
                email = it.arguments?.getString("email") ?: "-1",
                loggingOut = {
                    navController.navigate(Routes.register_screen.route)
                } ,
                backButton = {
                    navController.navigateUp()
                }
            )
        }
    }
}