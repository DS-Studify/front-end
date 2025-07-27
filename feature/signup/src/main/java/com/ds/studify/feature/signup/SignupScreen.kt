package com.ds.studify.feature.signup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun SignupRoute(
    paddingValues: PaddingValues
) {
    SignupScreen(paddingValues = paddingValues)
}


@Composable
internal fun SignupScreen(
    paddingValues: PaddingValues
) {}

@Preview
@Composable
private fun SignupScreenPreview() {
    SignupScreen(
        paddingValues = PaddingValues(0.dp)
    )
}
