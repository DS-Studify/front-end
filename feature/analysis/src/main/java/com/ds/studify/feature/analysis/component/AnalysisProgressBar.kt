package com.ds.studify.feature.analysis.component

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
fun AnalysisProgressBar(
    modifier: Modifier = Modifier,
    totalRecordTime: Int,
    totalStudyTime: Int
) {
    var isPlayAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        isPlayAnimation = true
    }

    val currentAnimatedValue = animateIntAsState(
        targetValue = if (isPlayAnimation) totalStudyTime else 0,
        animationSpec = tween(
            durationMillis = 1500
        ),
        label = "ProgressBar Animation",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(22.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(
                    20.dp + (currentAnimatedValue.value / totalRecordTime.toFloat()) *
                            with(LocalDensity.current) { constraints.maxWidth.toDp() - 20.dp }
                )
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(50),
                )
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFFDCE0),
                            Color(0xFFFD919F)
                        )
                    )
                )
        )
    }
}

@Preview
@Composable
private fun AnalysisProgressBarPreview() {
    AnalysisProgressBar(
        totalRecordTime = 8000,
        totalStudyTime = 5000
    )
}