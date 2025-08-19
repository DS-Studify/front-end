package com.ds.studify.feature.analysis

import android.util.Log
import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.AnalysisEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface AnalysisUiState {
    data object Loading : AnalysisUiState
    data class Data(
        val analysis: AnalysisEntity
    ) : AnalysisUiState

    data class Error(val message: String) : AnalysisUiState
}

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val studyRecordRepository: StudyRecordRepository
) : ViewModel(), ContainerHost<AnalysisUiState, Nothing> {

    override val container = container<AnalysisUiState, Nothing>(
        initialState = AnalysisUiState.Loading
    ) {
        loadAnalysis(studyRecordId = 28)
    }

    private fun loadAnalysis(studyRecordId: Int) = intent {
        val result = studyRecordRepository.getAnalysis(studyRecordId)
        Log.d("analLog", "시작")
        Log.d("analLog", result.toString())

        if (result.isSuccess) {
            val entity = result.getOrThrow()

            reduce {
                AnalysisUiState.Data(
                    analysis = result.getOrThrow()
                )
            }

        } else {
            AnalysisUiState.Error(result.exceptionOrNull()?.message ?: "오류 발생")
            val msg = result.exceptionOrNull()?.message.toString()
            Log.d("analLog", msg)
        }
    }

}