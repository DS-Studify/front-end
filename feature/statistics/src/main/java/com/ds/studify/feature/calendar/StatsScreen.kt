package com.ds.studify.feature.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithLogo
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.feature.calendar.component.StatsCalendar
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
    }
}

@Preview
@Composable
private fun StatsScreenPreview() {
    StatsScreen(
        paddingValues = PaddingValues(0.dp)
    )
}