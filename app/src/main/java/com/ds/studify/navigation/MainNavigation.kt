package com.ds.studify.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ds.studify.ui.MainRoute
import kotlinx.serialization.Serializable

@Serializable
data object RouteMain

fun NavGraphBuilder.mainScreen(

) {
    composable<RouteMain> {
        MainRoute()
    }
}