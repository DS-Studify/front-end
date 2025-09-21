package com.ds.studify.feature.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.component.StateTimeline
import com.ds.studify.core.designsystem.component.StudifyDonutChartWithState
import com.ds.studify.core.designsystem.component.StudifyTabBar
import com.ds.studify.core.designsystem.component.toChartSegments
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.designsystem.theme.pretendard
import com.ds.studify.core.domain.entity.AnalysisEntity
import com.ds.studify.core.domain.entity.PieChartEntity
import com.ds.studify.core.domain.entity.TimeEntry
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.core.ui.extension.formatTimeInKorean
import com.ds.studify.feature.analysis.component.AnalysisOutlinedButton
import com.ds.studify.feature.analysis.component.AnalysisPrimaryButton
import com.ds.studify.feature.analysis.component.AnalysisProgressBar
import com.ds.studify.feature.analysis.navigation.AnalysisNavigationDelegator
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun AnalysisRoute(
    navigationDelegator: AnalysisNavigationDelegator,
    viewModel: AnalysisViewModel = hiltViewModel()
) {

    val uiState by viewModel.collectAsState()

    when (uiState) {
        is AnalysisUiState.Data -> {
            val state = uiState as AnalysisUiState.Data
            AnalysisScreen(
                navigationDelegator = navigationDelegator,
                uiState = state,
                onTabEvent = {
                    viewModel.onEvent(AnalysisUiEvent.ChangeTabIndex(it))
                }
            )
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = StudifyColors.WHITE)
            )
        }
    }

}

@Composable
internal fun AnalysisScreen(
    navigationDelegator: AnalysisNavigationDelegator,
    uiState: AnalysisUiState.Data,
    onTabEvent: (Int) -> Unit
) {
    val tabList = listOf(
        stringResource(StudifyString.study_time),
        stringResource(StudifyString.focus),
        stringResource(StudifyString.pose)
    )
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val pagerState = rememberPagerState(
        initialPage = 0
    ) {
        tabList.size
    }
    val insets = WindowInsets.navigationBars.asPaddingValues()

    LaunchedEffect(pagerState.currentPage) {
        onTabEvent(pagerState.currentPage)
    }

    LaunchedEffect(uiState.currentTab) {
        pagerState.scrollToPage(uiState.currentTab)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StudifyColors.WHITE)
            .verticalScroll(scrollState)
            .padding(bottom = insets.calculateBottomPadding() + 40.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            color = StudifyColors.PK01,
            shadowElevation = 4.dp,
            shape = RoundedCornerShape(
                bottomStart = 20.dp,
                bottomEnd = 20.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(top = 40.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(StudifyString.analysis_title),
                        style = Typography.titleLarge,
                        color = StudifyColors.BLACK
                    )
                }

                Column(
                    modifier = Modifier
                        .padding(top = 40.dp, start = 40.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = stringResource(StudifyString.analysis_real_study_time),
                        style = Typography.titleSmall,
                        color = StudifyColors.BLACK
                    )
                    Text(
                        text = formatTimeInKorean(uiState.analysis.actualStudyTime.toInt()),
                        style = Typography.titleLarge,
                        color = StudifyColors.BLACK
                    )
                }

                AnalysisProgressBar(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    totalRecordTime = uiState.analysis.recordTime.toInt(),
                    totalStudyTime = uiState.analysis.actualStudyTime.toInt()
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                        .padding(top = 7.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = stringResource(StudifyString.analysis_record_time),
                        style = Typography.bodySmall,
                        color = Color(0xFF6B6D71)
                    )
                    Text(
                        text = formatTimeInKorean(uiState.analysis.recordTime.toInt()),
                        style = Typography.bodySmall,
                        color = Color(0xFF6B6D71)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp)
                        .offset(y = (-10).dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = stringResource(StudifyString.analysis_during_record),
                        style = Typography.bodySmall,
                        color = Color(0xFF6B6D71)
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = stringResource(
                            StudifyString.analysis_during_record_study,
                            ((uiState.analysis.actualStudyTime.toFloat() / uiState.analysis.recordTime.toFloat()) * 100).toInt()
                        ),
                        style = TextStyle(
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = StudifyColors.PK03
                    )
                }
            }
        }

        Text(
            text = stringResource(StudifyString.analysis_ai_feedback),
            style = Typography.headlineSmall,
            color = StudifyColors.BLACK,
            modifier = Modifier
                .padding(top = 27.dp, start = 24.dp, bottom = 10.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 27.dp)
                .background(
                    color = StudifyColors.PK01,
                    shape = RoundedCornerShape(10)
                )
        ) {
            Text(
                text = uiState.analysis.aiFeedback,
                style = Typography.bodySmall,
                modifier = Modifier
                    .padding(top = 23.dp, bottom = 40.dp)
                    .padding(horizontal = 15.dp)
            )
        }

        Column {
            StudifyTabBar(
                modifier = Modifier
                    .padding(horizontal = 30.dp),
                selectedTabIndex = pagerState.currentPage,
                onTabSelected = { pageIndex ->
                    scope.launch {
                        pagerState.scrollToPage(pageIndex)
                    }
                },
                tabTitles = tabList
            )
            HorizontalPager(
                modifier = Modifier
                    .padding(top = 24.dp),
                state = pagerState,
                userScrollEnabled = false,
                beyondViewportPageCount = 2
            ) { page ->
                when (page) {
                    0 -> StudifyDonutChartWithState(
                        chartData = uiState.pieChart.toChartSegments(),
                        stringResource(StudifyString.analysis_study_time_description)
                    )

                    1 -> StudifyDonutChartWithState(
                        chartData = uiState.pieChart.toChartSegments(),
                        stringResource(StudifyString.analysis_focus_description)
                    )

                    2 -> StudifyDonutChartWithState(
                        chartData = uiState.pieChart.toChartSegments(),
                        stringResource(StudifyString.analysis_pose_description)
                    )
                }
            }
        }

        Text(
            text = stringResource(StudifyString.analysis_state_timeline),
            style = Typography.headlineSmall,
            color = StudifyColors.BLACK,
            modifier = Modifier
                .padding(top = 50.dp, start = 24.dp, bottom = 10.dp)
        )

        StateTimeline(
            timeLogs = uiState.analysis.timeLog,
            startTime = uiState.analysis.startTime,
            endTime = uiState.analysis.endTime,
            modifier = Modifier
                .padding(horizontal = 20.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(26.dp, Alignment.CenterHorizontally)
        ) {
            AnalysisOutlinedButton(
                text = stringResource(StudifyString.analysis_restudy),
                onClick = navigationDelegator.onRestudyClick
            )
            AnalysisPrimaryButton(
                text = stringResource(StudifyString.analysis_finish),
                onClick = navigationDelegator.onStudyCloseClick
            )
        }
    }
}

@Preview
@Composable
private fun AnalysisScreenPreview() {

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

    AnalysisScreen(
        navigationDelegator = AnalysisNavigationDelegator(
            onRestudyClick = {},
            onStudyCloseClick = {}
        ),
        uiState = AnalysisUiState.Data(
            currentTab = 0,
            analysis = AnalysisEntity(
                studyRecordId = 1,
                studyDate = "2025년 7월 27일 (일)",
                startTime = "2025-07-27T23:00",
                endTime = "2025-07-27T23:30",
                recordTime = "1800",
                recordRatio = 13,
                actualStudyTime = "240",
                timeLog = timeLog,
                aiFeedback = "오늘 학습 태도를 분석한 결과, 전체적으로 집중력이 높은 편이었습니다. 다만, 중간중간 자세가 흐트러지거나 시선이 다른 곳으로 향하는 순간이 몇 번 감지되었습니다. 앞으로는 짧은 휴식을 적절히 활용하면서 자세를 바로잡는 습관을 들이면 더욱 효과적인 학습이 가능할 거에요."
            ),
            pieChart = listOf(
                PieChartEntity(
                    label = "공부",
                    time = 24300
                ),
                PieChartEntity(
                    label = "졸음",
                    time = 4200
                ),
                PieChartEntity(
                    label = "자리비움",
                    time = 2800
                ),
                PieChartEntity(
                    label = "기타",
                    time = 4800
                )
            )
        ),
        onTabEvent = {}
    )
}