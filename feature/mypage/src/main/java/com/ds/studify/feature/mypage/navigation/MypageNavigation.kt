package com.ds.studify.feature.mypage.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.ds.studify.feature.mypage.MypageRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteMypage

fun NavController.navigateToMypage(
    navOptions: NavOptions
) {
    navigate(RouteMypage, navOptions)
}

fun NavGraphBuilder.mypageScreen(
    paddingValues: PaddingValues
) {
    composable<RouteMypage> {
        MypageRoute(
            paddingValues = paddingValues
        )
    }
}