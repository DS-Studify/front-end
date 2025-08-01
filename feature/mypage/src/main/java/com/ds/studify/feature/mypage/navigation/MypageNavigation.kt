package com.ds.studify.feature.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ds.studify.feature.mypage.MyPageRoute
import kotlinx.serialization.Serializable

@Serializable
data object NavRouteMyPage

@Serializable
data object RouteMyPage

fun NavGraphBuilder.myPageScreen(
    navHostController: NavHostController
) {
    navigation<NavRouteMyPage>(
        startDestination = RouteMyPage
    ) {
        composable<RouteMyPage> {
            MyPageRoute(
                onBack = { navHostController.popBackStack() }
            )
        }
    }
}

fun NavHostController.navigateToMyPage() {
    navigate(NavRouteMyPage)
}