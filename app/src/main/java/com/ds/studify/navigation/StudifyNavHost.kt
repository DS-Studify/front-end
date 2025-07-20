package com.ds.studify.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import com.ds.studify.feature.analysis.navigation.AnalysisNavigationDelegator
import com.ds.studify.feature.analysis.navigation.RouteAnalysis
import com.ds.studify.feature.analysis.navigation.analysisScreen
import com.ds.studify.feature.analysis.navigation.navigateToAnalysis
import com.ds.studify.feature.camera.navigation.cameraScreen
import com.ds.studify.feature.camera.navigation.navigateToCamera
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator
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
                homeNavigationDelegator = HomeNavigationDelegator(
                    onStartToStudyClick = { navController.navigateToCamera() }
                )
            )

            cameraScreen(
                navController,
                navigateToAnalysis = { navController.navigateToAnalysis() }
            )

            analysisScreen(
                analysisNavigationDelegator = AnalysisNavigationDelegator(
                    onRestudyClick = { navController.navigateToCamera() },
                    onStudyCloseClick = {
                        navController.navigateToMain(
                            navOptions {
                                popUpTo(RouteAnalysis) { inclusive = true }
                            }
                        )
                    }
                )
            )
        }
    }
}