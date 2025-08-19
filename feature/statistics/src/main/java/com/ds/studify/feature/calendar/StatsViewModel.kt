package com.ds.studify.feature.calendar

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.CalendarDailyEntity
import com.ds.studify.core.domain.entity.CalendarMonthlyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

sealed interface StatsUiState {
    data object Loading : StatsUiState
    data class Data(
        val selectedYearMonth: YearMonth = YearMonth.now(),
        val selectedDate: LocalDate,
        val calendar: CalendarMonthlyEntity,
        val daily: CalendarDailyEntity
    ) : StatsUiState

    data class Error(val message: String) : StatsUiState
}

sealed class StatsUiEvent {
    data class ChangeYearMonth(val year: Int, val month: Int) : StatsUiEvent()
    data class ChangeDate(val date: Int) : StatsUiEvent()
}

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val studyRecordRepository: StudyRecordRepository
) : ViewModel(), ContainerHost<StatsUiState, Nothing> {

    override val container: Container<StatsUiState, Nothing> = container(
        initialState = StatsUiState.Loading
    ) {
        val currentYearMonth = YearMonth.now()
        val todayDate = LocalDate.now()

        loadMonthAndDay(currentYearMonth, todayDate)
    }

    private fun loadMonthAndDay(month: YearMonth, date: LocalDate) = intent {
        coroutineScope {
            val monthStr = month.toString()
            val dateStr = date.toString()

            val monthlyDeferred =
                async { studyRecordRepository.getCalendarMonthly(month = monthStr) }
            val dailyDeferred = async { studyRecordRepository.getCalendarDaily(date = dateStr) }

            val monthlyResult = monthlyDeferred.await()
            val dailyResult = dailyDeferred.await()

            val monthly = monthlyResult.getOrElse { err ->
                reduce { StatsUiState.Error(err.message ?: "월간 데이터 로드 실패") }
                return@coroutineScope
            }
            val daily = dailyResult.getOrElse { err ->
                reduce { StatsUiState.Error(err.message ?: "일간 데이터 로드 실패") }
                return@coroutineScope
            }

            reduce {
                StatsUiState.Data(
                    selectedYearMonth = month,
                    selectedDate = date,
                    calendar = monthly,
                    daily = daily
                )
            }
        }
    }

    fun onEvent(event: StatsUiEvent) {
        when (event) {
            is StatsUiEvent.ChangeYearMonth -> intent {
                if (state !is StatsUiState.Data) return@intent

                val dataState = state as StatsUiState.Data
                val selectedYearMonth = YearMonth.of(event.year, event.month)

                studyRecordRepository.getCalendarMonthly(month = selectedYearMonth.toString())
                    .onSuccess { response ->
                        reduce {
                            dataState.copy(
                                selectedYearMonth = selectedYearMonth,
                                calendar = response
                            )
                        }
                    }.onFailure { error ->
                        reduce {
                            StatsUiState.Error(error.message ?: "월간 데이터 로드 실패")
                        }
                    }
            }

            is StatsUiEvent.ChangeDate -> intent {
                if (state !is StatsUiState.Data) return@intent

                val dataState = state as StatsUiState.Data
                val selectedDate = LocalDate.of(
                    dataState.selectedYearMonth.year, dataState.selectedYearMonth.month, event.date
                )

                studyRecordRepository.getCalendarDaily(date = selectedDate.toString())
                    .onSuccess { response ->
                        reduce {
                            dataState.copy(
                                selectedDate = selectedDate,
                                daily = response
                            )
                        }
                    }.onFailure { error ->
                        reduce {
                            StatsUiState.Error(error.message ?: "일간 데이터 로드 실패")
                        }
                    }
            }
        }
    }
}