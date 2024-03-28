package com.varunkumar.echo_social_app.utils

sealed class Routes(val route: String) {
    data object splash_screen : Routes("splash_screen")
    data object home_screen : Routes("home_screen")
    data object register_screen : Routes("register_screen")
    data object add_post_screen: Routes("add_post_screen")
    data object profile_screen : Routes("profile_screen")
    data object search_screen: Routes("search_screen")
    data object post_screen : Routes("post_screen")
}