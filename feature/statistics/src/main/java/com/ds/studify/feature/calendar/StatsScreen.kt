package com.ds.studify.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.domain.entity.CalendarDailyEntity
import com.ds.studify.core.domain.entity.CalendarMonthlyEntity
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.core.ui.extension.formatTimeInKorean
import com.ds.studify.feature.calendar.component.StatsCalendar
import com.ds.studify.feature.calendar.component.StatsTimeLine
import org.orbitmvi.orbit.compose.collectAsState
import java.time.LocalDate
import java.time.YearMonth

@Composable
internal fun StatsRoute(
    onNavigateToFeedback: (Long) -> Unit,
    viewModel: StatsViewModel = hiltViewModel(),
    paddingValues: PaddingValues
) {
    val uiState by viewModel.collectAsState()

    when (uiState) {
        is StatsUiState.Data -> {
            val state = uiState as StatsUiState.Data
            StatsScreen(
                uiState = state,
                onEvent = viewModel::onEvent,
                onNavigateToFeedback = { id ->
                    onNavigateToFeedback(id)
                },
                paddingValues = paddingValues
            )
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = StudifyColors.WHITE)
            )
        }
    }
}

@Composable
internal fun StatsScreen(
    uiState: StatsUiState.Data,
    onEvent: (StatsUiEvent) -> Unit,
    onNavigateToFeedback: (Long) -> Unit,
    paddingValues: PaddingValues
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = StudifyColors.WHITE)
            .padding(
                top = 40.dp,
                bottom = paddingValues.calculateBottomPadding()
            )
            .verticalScroll(scrollState)
    ) {
        StatsCalendar(
            modifier = Modifier.fillMaxWidth(),
            yearMonthState = uiState.selectedYearMonth,
            dateState = uiState.selectedDate,
            studyTimeInMonth = uiState.calendar.calendar,
            onMonthPickerClick = {},
            onEvent = onEvent
        )

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            thickness = 10.dp,
            color = StudifyColors.PK01
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.daily.date,
                color = StudifyColors.BLACK,
                style = Typography.headlineSmall,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 26.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(
                        text = stringResource(StudifyString.stats_total_study_time),
                        color = StudifyColors.PK03,
                        style = Typography.headlineSmall,
                    )
                    Text(
                        text = formatTimeInKorean(uiState.daily.totalStudyTime),
                        color = StudifyColors.BLACK,
                        style = Typography.bodySmall,
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(
                        text = stringResource(StudifyString.stats_total_focus_time),
                        color = StudifyColors.PK03,
                        style = Typography.headlineSmall,
                    )
                    Text(
                        text = formatTimeInKorean(uiState.daily.focusTime),
                        color = StudifyColors.BLACK,
                        style = Typography.bodySmall,
                    )
                }
            }
        }

        StatsTimeLine(
            modifier = Modifier
                .padding(top = 60.dp, bottom = 30.dp)
                .padding(horizontal = 40.dp),
            studyTimes = uiState.daily.timeRanges,
            onClick = { clickedId ->
                onNavigateToFeedback(clickedId)
            }
        )
    }
}

@Preview
@Composable
private fun StatsScreenPreview() {
    StatsScreen(
        uiState = StatsUiState.Data(
            selectedYearMonth = YearMonth.now(),
            selectedDate = LocalDate.now(),
            calendar = CalendarMonthlyEntity(
                year = 2025,
                month = 8,
                calendar = listOf(
                    CalendarMonthlyEntity.CalendarInfo(
                        date = "2025-08-18",
                        totalStudyTime = 3600
                    )
                )
            ),
            daily = CalendarDailyEntity(
                date = "8월 18일 (월)",
                totalStudyTime = 3600,
                focusTime = 2400,
                timeRanges = listOf(
                    CalendarDailyEntity.StudyRecord(
                        studyRecordId = 1,
                        start = "01:00",
                        end = "01:50"
                    )
                )
            )
        ),
        onEvent = {},
        onNavigateToFeedback = {},
        paddingValues = PaddingValues(0.dp)
    )
}