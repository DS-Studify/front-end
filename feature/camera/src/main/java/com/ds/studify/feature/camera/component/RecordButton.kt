package com.ds.studify.feature.camera.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.ui.extension.clickableWithoutRippleEffect
import com.ds.studify.feature.camera.RecordingState

@Composable
internal fun RecordButton(
    modifier: Modifier = Modifier,
    recordingState: RecordingState,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clickableWithoutRippleEffect(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .size(86.dp)
                .padding(2.dp)
        ) {
            drawCircle(
                color = Color.White,
                style = Stroke(width = 4.dp.toPx())
            )
        }

        val innerSize = if (recordingState == RecordingState.OnRecord) 31.dp else 70.dp
        val shape =
            if (recordingState == RecordingState.OnRecord) RoundedCornerShape(10.dp) else CircleShape

        Box(
            modifier = Modifier
                .size(innerSize)
                .clip(shape)
                .background(Color(0xFFFF5858))
        )
    }
}

@Preview
@Composable
private fun StartRecordButtonPreview() {
    RecordButton(
        recordingState = RecordingState.Idle,
        onClick = {}
    )
}

@Preview
@Composable
private fun OnRecordButtonPreview() {
    RecordButton(
        recordingState = RecordingState.OnRecord,
        onClick = {}
    )
}