package com.ds.studify.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithLogo
import com.ds.studify.core.designsystem.component.StudifyStartButton
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    navigationDelegator: HomeNavigationDelegator
) {
    StudifyScaffoldWithLogo(
        paddingValues = paddingValues,
        leftActionButton = {
            IconButton(
                modifier = Modifier.size(width = 28.dp, height = 28.dp),
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(id = StudifyDrawable.ic_drawer),
                    contentDescription = null,
                    modifier = Modifier.size(width = 20.dp, height = 14.dp)
                )
            }
        }
    ) { innerPadding ->
        HomeScreen(
            paddingValues = innerPadding,
            navigationDelegator = navigationDelegator
        )
    }
}

@Composable
internal fun HomeScreen(
    paddingValues: PaddingValues,
    navigationDelegator: HomeNavigationDelegator
) {
    Box(
        modifier = Modifier
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = 0.dp,
                bottom = paddingValues.calculateBottomPadding()
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 27.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StudifyStartButton(
                onClick = navigationDelegator.onStartToStudyClick
            )
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        paddingValues = PaddingValues(top = 56.dp),
        navigationDelegator = HomeNavigationDelegator()
    )
}