package com.ds.studify.feature.camera.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ds.studify.feature.camera.CameraGuideRoute
import com.ds.studify.feature.camera.CameraRoute
import kotlinx.serialization.Serializable

@Serializable
data object NavRouteCamera

@Serializable
data object RouteCameraGuide

@Serializable
data object RouteCamera

fun NavGraphBuilder.cameraScreen(
    navHostController: NavHostController
) {
    navigation<NavRouteCamera>(
        startDestination = RouteCameraGuide
    ) {
        composable<RouteCameraGuide> {
            CameraGuideRoute(
                onBack = { navHostController.popBackStack() },
                onNavigateToStudy = {
                    navHostController.navigate(RouteCamera)
                }
            )
        }

        composable<RouteCamera> {
            CameraRoute()
        }
    }
}

fun NavHostController.navigateToCamera() {
    navigate(NavRouteCamera)
}