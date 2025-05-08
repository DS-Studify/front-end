package com.ds.studify.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
data object NavRouteMain

fun NavHostController.navigateToMain(
    navOptions: NavOptions? = null
) {
    navigate(NavRouteMain, navOptions)
}

@Composable
fun StudifyNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRouteMain,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation<NavRouteMain>(
            startDestination = RouteMain
        ) {

            mainScreen(

            )
        }
    }
}