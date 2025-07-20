package com.ds.studify.feature.analysis

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ds.studify.feature.analysis.navigation.AnalysisNavigationDelegator

@Composable
internal fun AnalysisRoute(
    navigationDelegator: AnalysisNavigationDelegator
) {
    AnalysisScreen(
        navigationDelegator = navigationDelegator
    )
}

@Composable
internal fun AnalysisScreen(
    navigationDelegator: AnalysisNavigationDelegator
) {

}

@Preview
@Composable
private fun AnalysisScreenPreview() {
    AnalysisScreen(
        navigationDelegator = AnalysisNavigationDelegator()
    )
}