package com.ds.studify.feature.analysis.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.analysis.AnalysisViewModel
import java.time.format.DateTimeFormatter


@Composable
fun StateTimeline(viewModel: AnalysisViewModel) {

    val processedData by viewModel.barDataList.collectAsState()
    val studyTimeRange by viewModel.studyTimeRange.collectAsState()

    val segmentMap = processedData.associate { barData ->
        barData.stateId to barData.segments
    }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun getStateColor(stateId: Int) = when (stateId) {
        1 -> StudifyColors.RED02
        2 -> StudifyColors.ORANGE
        3 -> StudifyColors.BLUE02
        4 -> StudifyColors.PURPLE02
        5 -> StudifyColors.YELLOW
        6 -> StudifyColors.GREEN
        else -> StudifyColors.G01
    }

    val labels = listOf(
        stringResource(StudifyString.state_1),
        stringResource(StudifyString.state_2),
        stringResource(StudifyString.state_3),
        stringResource(StudifyString.state_4),
        stringResource(StudifyString.state_5),
        stringResource(StudifyString.state_6)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(color = StudifyColors.PK01),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .padding(end = 3.dp)
        ) {
            Column(
                modifier = Modifier
                    .height(430.dp)
                    .padding(end = 7.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = studyTimeRange?.first?.format(timeFormatter) ?: "",
                    style = Typography.bodySmall
                )
                Text(
                    text = studyTimeRange?.second?.format(timeFormatter) ?: "",
                    style = Typography.bodySmall
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..6).forEach { stateId ->
                    Column(
                        modifier = Modifier
                            .height(470.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .width(47.dp)
                                .height(430.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = StudifyColors.G01)
                        ) {
                            segmentMap[stateId]?.forEach { segment ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(fraction = segment.height)
                                        .offset(y = (segment.startRatio * 430.dp.value).dp)
                                        .background(color = getStateColor(stateId))
                                )
                            }
                        }
                        Text(
                            modifier = Modifier
                                .padding(top = 5.dp),
                            text = labels[stateId - 1],
                            textAlign = TextAlign.Center,
                            style = Typography.bodySmall
                        )
                    }

                }

            }

        }

    }
}

@Preview
@Composable
private fun StateTimelinePreview() {
    val viewModel = AnalysisViewModel()
    StateTimeline(viewModel)
}