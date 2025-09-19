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
import com.ds.studify.feature.calendar.navigation.StatsNavigationDelegator
import com.ds.studify.feature.camera.navigation.cameraScreen
import com.ds.studify.feature.camera.navigation.navigateToCamera
import com.ds.studify.feature.feedback.navigation.feedbackScreen
import com.ds.studify.feature.feedback.navigation.navigateToFeedback
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator
import com.ds.studify.feature.login.navigation.LoginNavigationDelegator
import com.ds.studify.feature.login.navigation.RouteLogin
import com.ds.studify.feature.login.navigation.loginScreen
import com.ds.studify.feature.mypage.navigation.MyPageNavigationDelegator
import com.ds.studify.feature.mypage.navigation.myPageScreen
import com.ds.studify.feature.mypage.navigation.navigateToMyPage
import com.ds.studify.feature.mypage.navigation.navigateToNicknameChange
import com.ds.studify.feature.mypage.navigation.navigateToPasswordChange
import com.ds.studify.feature.signup.navigation.RouteSignup
import com.ds.studify.feature.signup.navigation.navigateToSignup
import com.ds.studify.feature.signup.navigation.signupScreen
import kotlinx.serialization.Serializable

@Serializable
data object NavRouteMain

@Serializable
data object NavRouteAuth

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
        startDestination = NavRouteAuth,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        navigation<NavRouteAuth>(
            startDestination = RouteLogin
        ) {
            loginScreen(
                navigationDelegator = LoginNavigationDelegator(
                    onLoginSuccess = {
                        navController.navigateToMain(
                            navOptions {
                                popUpTo(NavRouteAuth) { inclusive = true }
                                launchSingleTop = true
                            }
                        )
                    },
                    onSignInClicked = {
                        navController.navigateToSignup()
                    }
                )
            )

            signupScreen(
                onNavigateLogin = {
                    navController.navigate(RouteLogin) {
                        popUpTo(RouteSignup) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        navigation<NavRouteMain>(
            startDestination = RouteMain
        ) {

            mainScreen(
                homeNavigationDelegator = HomeNavigationDelegator(
                    onStartToStudyClick = { navController.navigateToCamera() },
                    onMyPageClick = { navController.navigateToMyPage() }
                ),
                statsNavigationDelegator = StatsNavigationDelegator(
                    onTimeLineClick = { id ->
                        navController.navigateToFeedback(id)
                    }
                )
            )

            cameraScreen(
                navController,
                navigateToAnalysis = { id ->
                    navController.navigateToAnalysis(id)
                }
            )

            analysisScreen(
                analysisNavigationDelegator = AnalysisNavigationDelegator(
                    onRestudyClick = { navController.navigateToCamera() },
                    onStudyCloseClick = {
                        navController.navigateToMain(
                            navOptions {
                                popUpTo<RouteAnalysis> { inclusive = true }
                            }
                        )
                    }
                )
            )

            feedbackScreen(navController)

            myPageScreen(
                navHostController = navController,
                navigationDelegator = MyPageNavigationDelegator(
                    onLogoutClick = {
                        navController.navigate(NavRouteAuth) {
                            popUpTo<NavRouteMain> { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onChangePasswordClick = { navController.navigateToPasswordChange() },
                    onChangeNicknameClick = { navController.navigateToNicknameChange() }
                )
            )
        }
    }
}