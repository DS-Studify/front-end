package com.ds.studify.feature.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ds.studify.feature.mypage.MyPageRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteMyPage

fun NavController.navigateToMyPage(
    navOptions: NavOptions
) {
    navigate(RouteMyPage, navOptions)
}

fun NavGraphBuilder.myPageScreen(
    paddingValues: PaddingValues
) {
    composable<RouteMyPage> {
        MyPageRoute(
            paddingValues = paddingValues
        )
    }
}