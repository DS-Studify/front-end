package com.ds.studify.feature.camera

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

@Composable
internal fun CameraGuideRoute(
    onBack: () -> Unit,
    onNavigateToStudy: () -> Unit
) {
    CameraGuideScreen(
        onBack = onBack,
        onNavigateToStudy = onNavigateToStudy
    )
}

@Composable
internal fun CameraGuideScreen(
    onBack: () -> Unit,
    onNavigateToStudy: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp)
                .padding(horizontal = 30.dp)
        ) {
            Text(
                text = "보다 정확한 분석을 위해, 카메라를 아래 가이드에 \n맞춰 설정해 주세요."
            )
        }

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
                onClick = onNavigateToStudy
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
private fun CameraGuideScreenPreview() {
    CameraGuideScreen(
        onBack = {},
        onNavigateToStudy = {}
    )
}