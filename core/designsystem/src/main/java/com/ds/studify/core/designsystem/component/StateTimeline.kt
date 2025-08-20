package com.ds.studify.core.designsystem.component

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.data.model.BarData
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.domain.entity.TimeEntry
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.core.ui.extension.getBarRatioInSeconds
import com.ds.studify.core.ui.extension.getTimeLog
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun StateTimeline(
    timeLogs: Map<String, List<TimeEntry>>,
    startTime: String,
    endTime: String,
    modifier: Modifier = Modifier
) {
    val isoFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val barDataList: List<BarData> = getBarRatioInSeconds(
        timeLogs = getTimeLog(timeLogs),
        start = LocalDateTime.parse(startTime, isoFormatter),
        end = LocalDateTime.parse(endTime, isoFormatter),
    )

    val segmentMap = barDataList.associate { barData ->
        barData.stateId to barData.segments
    }

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
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = StudifyColors.PK01)
            .padding(start = 6.dp, end = 14.dp)
            .padding(vertical = 12.dp)
    ) {
        Row {
            Column(
                modifier = Modifier
                    .height(430.dp)
                    .padding(end = 7.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = LocalDateTime.parse(startTime, isoFormatter).format(timeFormatter),
                    style = Typography.labelSmall
                )
                Text(
                    text = LocalDateTime.parse(endTime, isoFormatter).format(timeFormatter),
                    style = Typography.labelSmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..6).forEach { stateId ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
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
    val timeLog: Map<String, List<TimeEntry>> = mapOf(
        "1" to listOf(
            TimeEntry("2025-07-27T23:00:00", "2025-07-27T23:02:00"),
            TimeEntry("2025-07-27T23:10:00", "2025-07-27T23:12:00"),
        ),
        "2" to listOf(
            TimeEntry("2025-07-27T23:02:00", "2025-07-27T23:04:00"),
            TimeEntry("2025-07-27T23:12:00", "2025-07-27T23:14:00"),
        ),
        "3" to listOf(
            TimeEntry("2025-07-27T23:04:00", "2025-07-27T23:06:00"),
            TimeEntry("2025-07-27T23:14:00", "2025-07-27T23:16:00"),
        ),
        "4" to listOf(
            TimeEntry("2025-07-27T23:06:00", "2025-07-27T23:08:00"),
            TimeEntry("2025-07-27T23:16:00", "2025-07-27T23:18:00"),
        ),
        "5" to listOf(
            TimeEntry("2025-07-27T23:08:00", "2025-07-27T23:09:00"),
            TimeEntry("2025-07-27T23:18:00", "2025-07-27T23:19:00"),
        ),
        "6" to listOf(
            TimeEntry("2025-07-27T23:09:00", "2025-07-27T23:10:00"),
            TimeEntry("2025-07-27T23:19:00", "2025-07-27T23:20:00"),
        ),
    )

    StateTimeline(
        timeLogs = timeLog,
        startTime = "2025-07-27T23:00",
        endTime = "2025-07-27T23:30"
    )
}