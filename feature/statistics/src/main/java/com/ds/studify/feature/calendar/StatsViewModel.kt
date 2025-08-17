package com.ds.studify.feature.calendar

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.CalendarEntity
import dagger.hilt.android.lifecycle.HiltViewModel
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
        val calendar: CalendarEntity
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
        val now = LocalDate.now()

        loadCalendar(now)
    }

    private fun loadCalendar(date: LocalDate) = intent {
        val result = studyRecordRepository.getCalendar(date = date.toString())

        if (result.isSuccess) {
            reduce {
                StatsUiState.Data(
                    selectedDate = date,
                    calendar = result.getOrThrow()
                )
            }
        } else {
            reduce {
                StatsUiState.Error(result.exceptionOrNull()?.message ?: "알 수 없는 오류 발생")
            }
        }
    }

    fun onEvent(event: StatsUiEvent) {
        when (event) {
            is StatsUiEvent.ChangeYearMonth -> {
                val date = LocalDate.of(event.year, event.month, 1)

                loadCalendar(date)
            }

            is StatsUiEvent.ChangeDate -> intent {
                if (state !is StatsUiState.Data) return@intent

                val dataState = state as StatsUiState.Data
                val selectedDate = LocalDate.of(
                    dataState.selectedYearMonth.year, dataState.selectedYearMonth.month, event.date
                )

                loadCalendar(selectedDate)
            }
        }
    }
}