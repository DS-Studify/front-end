package com.ds.studify.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.studify.core.data.repository.StatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class HomeUiState(
    val todayStudyTime: Long
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel(), ContainerHost<HomeUiState, Nothing> {

    override val container: Container<HomeUiState, Nothing> = container(
        HomeUiState(todayStudyTime = 0L)
    ) {
        viewModelScope.launch {
            statsRepository.todayStudyTimeStream.collectLatest { studyTime ->
                reduce { state.copy(todayStudyTime = studyTime) }
            }
        }
    }
}