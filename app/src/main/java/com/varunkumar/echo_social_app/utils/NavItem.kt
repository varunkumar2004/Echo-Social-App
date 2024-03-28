package com.varunkumar.echo_social_app.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    val route: String,
    val dest: String,
    val icon: ImageVector
) {
    data object Home : NavItem("home", Routes.home_screen.route, Icons.Default.Home)
    data object AddPost: NavItem("add_post", Routes.add_post_screen.route, Icons.Default.AddCircleOutline)
    data object Search : NavItem("search", Routes.search_screen.route, Icons.Default.Search)
    data object Profile : NavItem("profile", Routes.profile_screen.route, Icons.Default.Person)
}