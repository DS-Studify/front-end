package com.ds.studify.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithLogo
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.calendar.component.StatsCalendar
import com.ds.studify.feature.calendar.component.StatsTimeLine
import java.time.YearMonth

@Composable
internal fun StatsRoute(
    paddingValues: PaddingValues
) {
    StudifyScaffoldWithLogo(
        paddingValues = paddingValues,
        leftActionButton = {
            IconButton(
                modifier = Modifier.size(width = 28.dp, height = 28.dp),
                onClick = { }
            ) {
                Icon(
                    painter = painterResource(id = StudifyDrawable.ic_drawer),
                    contentDescription = null,
                    modifier = Modifier.size(width = 20.dp, height = 14.dp)
                )
            }
        }
    ) { innerPadding ->
        StatsScreen(
            paddingValues = innerPadding
        )
    }
}

@Composable
internal fun StatsScreen(
    paddingValues: PaddingValues
) {
    Column(
        modifier = Modifier
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = 0.dp,
                bottom = paddingValues.calculateBottomPadding()
            )
            .fillMaxSize()
            .background(color = StudifyColors.WHITE)
    ) {
        StatsCalendar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingValues.calculateTopPadding()),
            yearMonthState = YearMonth.now(),
            studyTimeInMonth = emptyList(),
            onMonthPickerClick = {},
            onEvent = {}
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
                text = "7월 6일 (일)",
                color = StudifyColors.BLACK,
                style = Typography.headlineSmall,
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
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
                        text = "10시간 34분",
                        color = StudifyColors.BLACK,
                        style = Typography.bodySmall,
                    )
                }

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
                        text = "7시간 34분",
                        color = StudifyColors.BLACK,
                        style = Typography.bodySmall,
                    )
                }
            }
        }

        StatsTimeLine(
            modifier = Modifier
                .padding(top = 60.dp)
                .padding(horizontal = 40.dp),
            studyTimes = listOf("10:00~13:00", "14:30~18:33", "19:40~23:04"),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun StatsScreenPreview() {
    StatsScreen(
        paddingValues = PaddingValues(0.dp)
    )
}