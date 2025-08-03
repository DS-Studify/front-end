package com.ds.studify.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.resources.StudifyDrawable

internal val APP_LOGO_TITLE_HEIGHT_DP = 56.dp

@Composable
fun StudifyScaffoldWithLogo(
    paddingValues: PaddingValues,
    leftActionButton: @Composable (RowScope.() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Box {
        content(
            PaddingValues(
                top = paddingValues.calculateTopPadding() + APP_LOGO_TITLE_HEIGHT_DP,
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                bottom = paddingValues.calculateBottomPadding()
            )
        )
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(APP_LOGO_TITLE_HEIGHT_DP)
                .background(color = StudifyColors.WHITE)
        ) {
            leftActionButton?.let {
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 21.dp)
                ) {
                    leftActionButton()
                }
            }

            Image(
                painter = painterResource(id = StudifyDrawable.app_logo_title),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun StudifyScaffoldWithTitle(
    title: String,
    paddingValues: PaddingValues? = null,
    onBackButtonClick: (() -> Unit)? = null,
    @DrawableRes navIcon: Int = StudifyDrawable.ic_back,
    color: Color = StudifyColors.BLACK,
    content: @Composable (PaddingValues) -> Unit
) {
    paddingValues?.let {
        Box {
            content(
                PaddingValues(
                    top = it.calculateTopPadding() + APP_LOGO_TITLE_HEIGHT_DP,
                    start = it.calculateStartPadding(LocalLayoutDirection.current),
                    end = it.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = it.calculateBottomPadding()
                )
            )
            StudifyTitleBar(
                title = title,
                onBackButtonClick = onBackButtonClick,
                navIcon = navIcon,
                color = color
            )
        }
    } ?: run {
        Scaffold(
            topBar = {
                StudifyTitleBar(
                    title = title,
                    onBackButtonClick = onBackButtonClick,
                    navIcon = navIcon,
                    color = color
                )
            }
        ) { innerPadding ->
            content(innerPadding)
        }
    }
}