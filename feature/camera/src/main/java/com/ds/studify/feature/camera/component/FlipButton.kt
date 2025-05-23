package com.ds.studify.feature.camera.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.ui.extension.clickableWithoutRippleEffect

@Composable
internal fun FlipButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(49.dp)
            .clip(CircleShape)
            .background(
                Color.Black.copy(alpha = 0.31f)
            )
            .clickableWithoutRippleEffect(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = com.ds.studify.core.resources.R.drawable.ic_flip),
            contentDescription = "flip",
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Preview
@Composable
private fun FlipButtonPreview() {
    FlipButton(
        onClick = {}
    )
}