package com.ds.studify.feature.analysis.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.core.ui.extension.formatTimeInKorean
import kotlin.math.cos
import kotlin.math.sin

data class ChartSegment(val label: String, val color: Color, val time: Int)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DonutChartWithState(
    chartData: List<ChartSegment>,
    description: String? = null,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.Center)
            ) {
                DonutChart(chartData)
            }

            if (description != null) {
                IconButton(
                    modifier = Modifier
                        .padding(top = 10.dp, end = 40.dp)
                        .size(24.dp)
                        .align(Alignment.TopEnd),
                    onClick = { showDialog = true },
                ) {
                    Image(
                        painter = painterResource(id = StudifyDrawable.ic_info),
                        contentDescription = "info"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (chartData.size == 2) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                chartData.forEach {
                    StateItem(it.color, "${it.label} (${formatTimeInKorean(it.time)})")
                }
            }
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                chartData.forEach {
                    StateItem(it.color, "${it.label} (${formatTimeInKorean(it.time)})")
                }
            }
        }
    }

    if (description != null) {
        DescriptionDialog(
            description = description,
            showDialog = showDialog,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
private fun DonutChart(data: List<ChartSegment>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        var startAngle = -90f
        val strokeWidth = 50.dp.toPx()
        val diameter = size.minDimension
        val radius = diameter / 2
        val radiusOffset = strokeWidth / 2f
        val arcSize = Size(diameter - strokeWidth, diameter - strokeWidth)
        val arcOffset = Offset(radiusOffset, radiusOffset)
        val totalTime = data.sumOf { it.time }.toFloat()

        data.forEach { segment ->
            val percentage = if (totalTime == 0f) 0f else (segment.time / totalTime)
            val sweepAngle = percentage * 360f
            drawArc(
                color = segment.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                size = arcSize,
                topLeft = arcOffset
            )

            val midAngle = startAngle + sweepAngle / 2
            val angleRad = Math.toRadians(midAngle.toDouble())
            val textRadius = radius - strokeWidth / 1.8f
            val textX = center.x + textRadius * cos(angleRad).toFloat()
            val textY = center.y + textRadius * sin(angleRad).toFloat()

            drawContext.canvas.nativeCanvas.drawText(
                "${(percentage * 100).toInt()}%",
                textX,
                textY,
                Paint().apply {
                    color = android.graphics.Color.WHITE
                    textSize = 40f
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
            )

            startAngle += sweepAngle
        }
    }
}

@Composable
private fun StateItem(color: Color, text: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .width(150.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = Typography.bodySmall
        )
    }
}

@Composable
private fun DescriptionDialog(
    description: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss
) {
    if (showDialog) {
        AlertDialog(
            containerColor = StudifyColors.WHITE,
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = StudifyColors.G03
                    )
                ) {
                    Text(
                        text = stringResource(StudifyString.confirm),
                        color = StudifyColors.PK03
                    )
                }
            },
            text = {
                Text(
                    text = description,
                    color = StudifyColors.BLACK
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DonutChartWithFourStatePreview() {
    DonutChartWithState(
        listOf(
            ChartSegment("공부", StudifyColors.PK02, 24300),
            ChartSegment("졸음", StudifyColors.G03, 4200),
            ChartSegment("자리비움", StudifyColors.G02, 2800),
            ChartSegment("기타", StudifyColors.G01, 4800)
        ),
        "<기타> 는 책상에 앉아있었지만 공부하지 않은 시간으로, 졸음을 제외한 시간입니다.\n\nex.멍때림, 벽보기, 핸드폰 하기"
    )
}

@Preview(showBackground = true)
@Composable
private fun DonutChartWithTwoStatePreview() {
    DonutChartWithState(
        listOf(
            ChartSegment("집중", StudifyColors.PK02, 24300),
            ChartSegment("비집중", StudifyColors.G01, 12000),
        ),
        "*공부 시간 중 집중 비율"
    )
}

@Preview(showBackground = true)
@Composable
private fun DonutChartWithTwoStatePreview2() {
    DonutChartWithState(
        listOf(
            ChartSegment("바른 자세", StudifyColors.PK02, 24300),
            ChartSegment("나쁜 자세", StudifyColors.G01, 12000),
        ),
        "*공부 시간 중 자세의 비율"
    )
}