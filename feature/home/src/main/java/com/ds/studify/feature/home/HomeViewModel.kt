package com.ds.studify.feature.home

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.HomeEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val home: HomeEntity) : HomeUiState
    data class Error(val message: String) : HomeUiState
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val studyRecordRepository: StudyRecordRepository
) : ViewModel(), ContainerHost<HomeUiState, Nothing> {

    override val container = container<HomeUiState, Nothing>(
        initialState = HomeUiState.Loading
    ) {
        loadHome()
    }

    private fun loadHome() = intent {
        val result = studyRecordRepository.getHome()

        if (result.isSuccess) {
            reduce {
                HomeUiState.Success(result.getOrThrow())
            }
        } else {
            reduce {
                HomeUiState.Error(result.exceptionOrNull()?.message ?: "알 수 없는 오류 발생")
            }
        }
    }
}