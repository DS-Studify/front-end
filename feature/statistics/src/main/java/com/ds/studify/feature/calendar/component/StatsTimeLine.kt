package com.ds.studify.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.resources.StudifyDrawable

@Composable
fun StatsTimeLine(
    modifier: Modifier = Modifier,
    studyTimes: List<String>,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
    ) {
        studyTimes.forEachIndexed { index, time ->
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { onClick() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(StudifyColors.PK03)
                    )

                    Text(
                        text = time,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Image(
                        painter = painterResource(id = StudifyDrawable.ic_calendar_right),
                        contentDescription = "분석 보기",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(width = 11.dp, height = 22.dp)
                    )
                }

                // 아래 세로선은 마지막 항목 제외
                if (index != studyTimes.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(start = 3.5.dp)
                            .width(1.dp)
                            .height(40.dp)
                            .background(StudifyColors.G02)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatsTimeLinePreview() {
    StatsTimeLine(
        studyTimes = listOf("10:00~13:00", "14:30~18:33", "19:40~23:04"),
        onClick = {}
    )
}