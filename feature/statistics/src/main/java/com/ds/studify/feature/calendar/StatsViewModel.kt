package com.ds.studify.feature.calendar

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

data class StudyHistoryUiState(
    val yearMonth: YearMonth = YearMonth.now(),
    val studyHistoryInMonth: List<String> = emptyList()
)

data class DailyStatsUiState(
    val date: String,
    val focusTime: String,
    val studyTime: String,
    val studyTimeLine: List<String> = emptyList()
)

sealed interface StatsUiState {
    data object Loading : StatsUiState

    data class Data(
        val history: StudyHistoryUiState,
        val daily: DailyStatsUiState
    ) : StatsUiState
}

sealed class StatsUiEvent {
    data class ChangeYearMonth(val year: Int, val month: Int) : StatsUiEvent()
    data class ChangeDate(val date: Int) : StatsUiEvent()
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel(), ContainerHost<StatsUiState, Nothing> {

    override val container: Container<StatsUiState, Nothing> = container(
        initialState = StatsUiState.Loading
    ) {
        yearMonthState
            .map { yearMonth ->
                val history = StudyHistoryUiState(
                    yearMonth = yearMonth,
                    studyHistoryInMonth = statsRepository.getStudyHistoryInMonth(
                        yearMonth.year,
                        yearMonth.monthValue
                    )
                )
                val daily = DailyStatsUiState(
                    date = formatDateWithDayOfWeek(
                        yearMonth.year,
                        yearMonth.monthValue,
                        LocalDate.now().dayOfMonth
                    ),
                    focusTime = statsRepository.getDailyFocusTime(
                        yearMonth.year,
                        yearMonth.monthValue
                    ),
                    studyTime = statsRepository.getDailyStudyTime(
                        yearMonth.year,
                        yearMonth.monthValue,
                        LocalDate.now().dayOfMonth
                    ),
                    studyTimeLine = statsRepository.getDailyStudyTimeLine(
                        yearMonth.year,
                        yearMonth.monthValue
                    )
                )
                StatsUiState.Data(history = history, daily = daily)
            }
            .collectLatest {
                reduce { it }
            }
    }

    private val yearMonthState = MutableStateFlow(YearMonth.now())

    private fun formatDateWithDayOfWeek(year: Int, month: Int, day: Int): String {
        val date = LocalDate.of(year, month, day)
        val dayOfWeekKorean = when (date.dayOfWeek) {
            java.time.DayOfWeek.MONDAY -> "월"
            java.time.DayOfWeek.TUESDAY -> "화"
            java.time.DayOfWeek.WEDNESDAY -> "수"
            java.time.DayOfWeek.THURSDAY -> "목"
            java.time.DayOfWeek.FRIDAY -> "금"
            java.time.DayOfWeek.SATURDAY -> "토"
            else -> "일"
        }
        return "${month}월 ${day}일 ($dayOfWeekKorean)"
    }

    fun onEvent(event: StatsUiEvent) {
        when (event) {
            is StatsUiEvent.ChangeYearMonth -> {
                yearMonthState.value = YearMonth.of(event.year, event.month)
            }

            is StatsUiEvent.ChangeDate -> intent {
                if (state !is StatsUiState.Data) return@intent

                val dataState = state as StatsUiState.Data
                val yearMonth = yearMonthState.value

                val formattedDate =
                    formatDateWithDayOfWeek(yearMonth.year, yearMonth.monthValue, event.date)
                val focusTime =
                    statsRepository.getDailyFocusTime(yearMonth.year, yearMonth.monthValue)
                val studyTime = statsRepository.getDailyStudyTime(
                    yearMonth.year,
                    yearMonth.monthValue,
                    event.date
                )
                val studyTimeLine =
                    statsRepository.getDailyStudyTimeLine(yearMonth.year, yearMonth.monthValue)

                reduce {
                    dataState.copy(
                        daily = DailyStatsUiState(
                            date = formattedDate,
                            focusTime = focusTime,
                            studyTime = studyTime,
                            studyTimeLine = studyTimeLine
                        )
                    )
                }
            }
        }
    }
}