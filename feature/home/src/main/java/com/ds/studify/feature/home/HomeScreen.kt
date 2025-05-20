package com.ds.studify.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator

@Composable
internal fun HomeRoute(
    navigationDelegator: HomeNavigationDelegator
) {
    HomeScreen(navigationDelegator)
}

@Composable
internal fun HomeScreen(
    navigationDelegator: HomeNavigationDelegator
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextButton(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(vertical = 20.dp)
                    .fillMaxWidth()
                    .height(57.dp),
                colors = ButtonDefaults.textButtonColors(),
                onClick = navigationDelegator.onStartToStudyClick
            ) {
                Text(
                    text = "공부 시작"
                )
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        navigationDelegator = HomeNavigationDelegator()
    )
}