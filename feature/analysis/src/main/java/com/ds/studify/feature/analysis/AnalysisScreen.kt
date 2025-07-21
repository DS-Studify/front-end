package com.ds.studify.feature.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.designsystem.theme.pretendard
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.core.ui.extension.formatTimeInKorean
import com.ds.studify.feature.analysis.component.AnalysisProgressBar
import com.ds.studify.feature.analysis.navigation.AnalysisNavigationDelegator

@Composable
internal fun AnalysisRoute(
    navigationDelegator: AnalysisNavigationDelegator
) {
    AnalysisScreen(
        navigationDelegator = navigationDelegator
    )
}

@Composable
internal fun AnalysisScreen(
    navigationDelegator: AnalysisNavigationDelegator
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StudifyColors.WHITE)
            .verticalScroll(scrollState)
            .padding(bottom = 40.dp)
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
                        text = formatTimeInKorean(24300),
                        style = Typography.titleLarge,
                        color = StudifyColors.BLACK
                    )
                }

                AnalysisProgressBar(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    totalRecordTime = 32300,
                    totalStudyTime = 24300
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
                        text = formatTimeInKorean(32300),
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
                        text = stringResource(StudifyString.analysis_during_record_study, 85),
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
    }
}

@Preview
@Composable
private fun AnalysisScreenPreview() {
    AnalysisScreen(
        navigationDelegator = AnalysisNavigationDelegator()
    )
}