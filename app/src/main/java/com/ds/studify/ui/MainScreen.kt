package com.ds.studify.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator
import com.ds.studify.feature.home.navigation.RouteHome
import com.ds.studify.feature.home.navigation.homeScreen

@Composable
fun MainRoute(
    homeNavigationDelegator: HomeNavigationDelegator
) {
    val navController = rememberNavController()

    MainScreen(
        homeNavigationDelegator = homeNavigationDelegator,
        navController = navController
    )
}

@Composable
fun MainScreen(
    homeNavigationDelegator: HomeNavigationDelegator,
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = {

        }
    ) { innerPadding ->
        Box {
            NavHost(
                navController = navController,
                startDestination = RouteHome,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None }
            ) {
                homeScreen(
                    homeNavigationDelegator,
                    innerPadding
                )
            }
        }
    }
}