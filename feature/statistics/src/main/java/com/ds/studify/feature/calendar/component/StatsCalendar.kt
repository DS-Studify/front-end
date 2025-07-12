package com.ds.studify.feature.calendar.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.calendar.StatsUiEvent
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.ceil

@Composable
fun StatsCalendar(
    modifier: Modifier = Modifier,
    yearMonthState: YearMonth,
    dateState: LocalDate,
    studyTimeInMonth: List<String>,
    onMonthPickerClick: () -> Unit,
    onEvent: (StatsUiEvent) -> Unit
) {
    val yearMonth = remember(yearMonthState) {
        yearMonthState
    }

    Column(
        modifier = modifier
            .background(StudifyColors.WHITE)
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CalendarHeader(
            currentYearMonth = yearMonth,
            onPreviousClick = {
                yearMonth.minusMonths(1).run {
                    onEvent(
                        StatsUiEvent.ChangeYearMonth(
                            year = year,
                            month = monthValue
                        )
                    )
                }
            },
            onNextClick = {
                yearMonth.plusMonths(1).run {
                    onEvent(
                        StatsUiEvent.ChangeYearMonth(
                            year = year,
                            month = monthValue
                        )
                    )
                }
            },
            onMonthPickerClick = onMonthPickerClick
        )

        CalendarDate(
            currentMonth = yearMonth,
            selectedDate = dateState,
            studyTimeInMonth = studyTimeInMonth,
            onDateClick = { date ->
                onEvent(
                    StatsUiEvent.ChangeDate(date)
                )
            }
        )
    }
}

@Composable
internal fun CalendarHeader(
    currentYearMonth: YearMonth,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onMonthPickerClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = StudifyDrawable.ic_calendar_left),
            contentDescription = "이전 달로 이동",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onPreviousClick() }
                .size(44.dp)
        )
        Text(
            text = "${currentYearMonth.year}.${currentYearMonth.monthValue}",
            color = StudifyColors.PK03,
            style = Typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 40.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                ) { onMonthPickerClick() }
        )
        Image(
            painter = painterResource(id = StudifyDrawable.ic_calendar_right),
            contentDescription = "다음 달로 이동",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onNextClick() }
                .size(44.dp)
        )
    }
}

@Composable
internal fun CalendarDate(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    studyTimeInMonth: List<String>,
    onDateClick: (Int) -> Unit
) {
    val currentDate = LocalDate.now()
    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfMonth = currentMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value
    val weeksInMonth = ceil((firstDayOfWeek - 1 + daysInMonth) / 7.0).toInt()

    Column(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 20.dp)
        ) {
            listOf(
                stringResource(StudifyString.sunday),
                stringResource(StudifyString.monday),
                stringResource(StudifyString.tuesday),
                stringResource(StudifyString.wednesday),
                stringResource(StudifyString.thursday),
                stringResource(StudifyString.friday),
                stringResource(StudifyString.saturday)
            ).forEach { day ->
                Text(
                    text = day,
                    color = when (day) {
                        stringResource(StudifyString.sunday) -> StudifyColors.PK03
                        stringResource(StudifyString.saturday) -> StudifyColors.BLUE
                        else -> StudifyColors.BLACK
                    },
                    style = Typography.headlineSmall,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                )
            }
        }

        (1..weeksInMonth + 1).forEach { week ->
            Row {
                (1..7).forEach { dayOfWeek ->
                    val day = (week - 1) * 7 + dayOfWeek - firstDayOfWeek
                    val isSunday = dayOfWeek == 1
                    val isSaturday = dayOfWeek == 7

                    if (day in 1..daysInMonth) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(bottom = 8.dp)
                                .height(38.dp)
                                .clickable {
                                    onDateClick(day)
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(24.dp),
                                    shape = CircleShape,
                                    color = if (selectedDate == currentMonth.atDay(day)) StudifyColors.PK03
                                    else if (currentDate == currentMonth.atDay(day)) StudifyColors.G01
                                    else Color.Transparent
                                ) {
                                    Text(
                                        text = day.toString(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .wrapContentHeight(align = Alignment.CenterVertically),
                                        style = TextStyle(
                                            fontFamily = Typography.titleSmall.fontFamily,
                                            fontWeight = Typography.titleSmall.fontWeight,
                                            fontSize = Typography.titleSmall.fontSize,
                                            lineHeight = Typography.titleSmall.lineHeight,
                                            baselineShift = BaselineShift(0f),
                                        ),
                                        color = if (selectedDate == currentMonth.atDay(day)) StudifyColors.WHITE
                                        else if (isSunday) StudifyColors.PK02
                                        else if (isSaturday) StudifyColors.BLUE
                                        else StudifyColors.BLACK
                                    )
                                }
                            }
                            if (currentMonth.atDay(day) <= selectedDate) {
                                val studyTimeAtDay = studyTimeInMonth.getOrElse(day - 1) { "" }
                                Text(
                                    text = studyTimeAtDay,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(align = Alignment.CenterVertically),
                                    style = Typography.labelSmall,
                                    color = StudifyColors.BLACK,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 393, heightDp = 400)
@Composable
private fun StatsCalendarPreview() {
    StatsCalendar(
        yearMonthState = YearMonth.now(),
        dateState = LocalDate.now(),
        studyTimeInMonth = listOf("1H 10M", "2H 20M"),
        onMonthPickerClick = {},
        onEvent = {}
    )
}