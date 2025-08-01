package com.ds.studify.feature.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.resources.StudifyString

@Composable
internal fun FeedbackRoute(
    onBack: () -> Unit
) {
    StudifyScaffoldWithTitle(
        titleId = StudifyString.camera_guide_title,
        onBackButtonClick = onBack
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            FeedbackScreen(
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
internal fun FeedbackScreen(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StudifyColors.WHITE)
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {

    }
}

@Preview
@Composable
private fun FeedbackScreenPreview() {
    FeedbackScreen(
        paddingValues = PaddingValues(0.dp)
    )
}