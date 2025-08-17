package com.ds.studify.feature.mypage.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ds.studify.feature.mypage.MyPageRoute
import com.ds.studify.feature.mypage.NicknameChangeRoute
import com.ds.studify.feature.mypage.PasswordChangeRoute
import kotlinx.serialization.Serializable

@Serializable
data object NavRouteMyPage

@Serializable
data object RouteMyPage

@Serializable
data object RoutePasswordChange

@Serializable
data object RouteNicknameChange

fun NavGraphBuilder.myPageScreen(
    navHostController: NavHostController,
    navigationDelegator: MyPageNavigationDelegator
) {
    navigation<NavRouteMyPage>(
        startDestination = RouteMyPage
    ) {
        composable<RouteMyPage> {
            MyPageRoute(
                onBack = { navHostController.popBackStack() },
                navigationDelegator = navigationDelegator
            )
        }

        composable<RoutePasswordChange> {
            PasswordChangeRoute(
                onBack = { navHostController.popBackStack() }
            )
        }

        composable<RouteNicknameChange> {
            NicknameChangeRoute(
                onBack = { navHostController.popBackStack() }
            )
        }
    }
}

fun NavHostController.navigateToMyPage() {
    navigate(NavRouteMyPage)
}

fun NavHostController.navigateToPasswordChange() {
    navigate(RoutePasswordChange)
}

fun NavHostController.navigateToNicknameChange() {
    navigate(RouteNicknameChange)
}

class MyPageNavigationDelegator(
    val onLogoutClick: () -> Unit = {},
    val onChangePasswordClick: () -> Unit = {},
    val onChangeNicknameClick: () -> Unit = {}
)