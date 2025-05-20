package com.ds.studify.ui

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
    NavHost(
        navController = navController,
        startDestination = RouteHome
    ) {
        homeScreen(
            homeNavigationDelegator
        )
    }
}