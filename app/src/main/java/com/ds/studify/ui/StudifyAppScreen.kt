package com.ds.studify.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ds.studify.navigation.StudifyNavHost

@Composable
fun StudifyAppRoute() {
    StudifyApp()
}

@Composable
fun StudifyApp() {
    val navController: NavHostController = rememberNavController()

    StudifyNavHost(navController)
}