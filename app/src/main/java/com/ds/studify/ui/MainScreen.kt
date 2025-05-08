package com.ds.studify.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun MainRoute(

) {
    val navController = rememberNavController()

    MainScreen(
        navController = navController
    )
}

@Composable
fun MainScreen(
    navController: NavHostController,
) {

}