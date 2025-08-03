package com.ds.studify.feature.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.component.ChartSegment
import com.ds.studify.core.designsystem.component.StudifyDonutChartWithState
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.component.StudifyTabBar
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.designsystem.theme.pretendard
import com.ds.studify.core.resources.StudifyString
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun FeedbackRoute(
    onBack: () -> Unit
) {
    val viewModel = hiltViewModel<FeedbackViewModel>()
    val uiState by viewModel.collectAsState()

    when (val state = uiState) {
        FeedbackState.Loading -> {}
        is FeedbackState.Screen -> {
            StudifyScaffoldWithTitle(
                title = state.studyDate,
                onBackButtonClick = onBack
            ) { paddingValues ->
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    FeedbackScreen(
                        paddingValues = paddingValues,
                        uiState = state,
                        onTabEvent = {
                            viewModel.onEvent(FeedbackUiEvent.ChangeTabIndex(it))
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun FeedbackScreen(
    paddingValues: PaddingValues,
    uiState: FeedbackState.Screen,
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
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
            .verticalScroll(scrollState)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = Color(0xFFFFFCFC),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = StudifyColors.PK03,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(StudifyString.feedback_real_study_time),
                        color = StudifyColors.BLACK,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = "6시간 45분",
                        color = StudifyColors.PK03,
                        style = Typography.headlineSmall
                    )
                }
            }
        }

        StudifyTabBar(
            modifier = Modifier.padding(horizontal = 30.dp),
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
                .padding(top = 30.dp),
            state = pagerState,
            userScrollEnabled = false,
            beyondViewportPageCount = 2
        ) { page ->
            when (page) {
                0 -> StudifyDonutChartWithState(
                    listOf(
                        ChartSegment(
                            stringResource(StudifyString.study),
                            StudifyColors.PK02,
                            24300
                        ),
                        ChartSegment(
                            stringResource(StudifyString.sleep),
                            StudifyColors.G03,
                            4200
                        ),
                        ChartSegment(
                            stringResource(StudifyString.emptiness_of_seat),
                            StudifyColors.G02,
                            2800
                        ),
                        ChartSegment(stringResource(StudifyString.etc), StudifyColors.G01, 4800)
                    ),
                    stringResource(StudifyString.analysis_study_time_description)
                )

                1 -> StudifyDonutChartWithState(
                    listOf(
                        ChartSegment(
                            stringResource(StudifyString.focus),
                            StudifyColors.PK02,
                            24300
                        ),
                        ChartSegment(
                            stringResource(StudifyString.not_focus),
                            StudifyColors.G01,
                            12000
                        ),
                    ),
                    stringResource(StudifyString.analysis_focus_description)
                )

                2 -> StudifyDonutChartWithState(
                    listOf(
                        ChartSegment(
                            stringResource(StudifyString.good_pose),
                            StudifyColors.PK02,
                            24300
                        ),
                        ChartSegment(
                            stringResource(StudifyString.bad_pose),
                            StudifyColors.G01,
                            12000
                        ),
                    ),
                    stringResource(StudifyString.analysis_pose_description)
                )
            }
        }

        Text(
            text = stringResource(StudifyString.analysis_ai_feedback),
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = StudifyColors.BLACK,
            modifier = Modifier
                .padding(top = 40.dp, start = 30.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
                .background(
                    color = StudifyColors.PK01,
                    shape = RoundedCornerShape(10)
                )
        ) {
            Text(
                text = "오늘 학습 태도를 분석한 결과, 전체적으로 집중력이 높은 편이었습니다. 다만, 중간중간 자세가 흐트러지거나 시선이 다른 곳으로 향하는 순간이 몇 번 감지되었습니다. 앞으로는 짧은 휴식을 적절히 활용하면서 자세를 바로잡는 습관을 들이면 더욱 효과적인 학습이 가능할 거에요.",
                style = Typography.bodySmall,
                modifier = Modifier
                    .padding(top = 23.dp, bottom = 40.dp)
                    .padding(horizontal = 15.dp)
            )
        }

        //TODO: 타임라인
    }
}

@Preview
@Composable
private fun FeedbackScreenPreview() {
    FeedbackScreen(
        paddingValues = PaddingValues(0.dp),
        uiState = FeedbackState.Screen(
            currentTab = 0,
            studyDate = "2025년 8월 3일"
        ),
        onTabEvent = {}
    )
}