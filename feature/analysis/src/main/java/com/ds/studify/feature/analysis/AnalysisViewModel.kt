package com.ds.studify.feature.analysis

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.data.repository.StudyRepository
import com.ds.studify.core.domain.entity.AnalysisEntity
import com.ds.studify.core.domain.entity.PieChartEntity
import com.ds.studify.feature.analysis.navigation.RouteAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface AnalysisUiState {
    data object Loading : AnalysisUiState
    data class Data(
        val currentTab: Int,
        val analysis: AnalysisEntity,
        val pieChart: List<PieChartEntity>
    ) : AnalysisUiState

    data class Error(val message: String) : AnalysisUiState
}

sealed interface AnalysisUiEvent {
    data class ChangeTabIndex(val index: Int) : AnalysisUiEvent
}

sealed interface AnalysisSideEffect {
    data object InvalidAnalysisId : AnalysisSideEffect
}

@HiltViewModel
class AnalysisViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val studyRepository: StudyRepository,
    private val studyRecordRepository: StudyRecordRepository
) : ViewModel(), ContainerHost<AnalysisUiState, AnalysisSideEffect> {

    override val container = container<AnalysisUiState, AnalysisSideEffect>(
        initialState = AnalysisUiState.Loading
    ) {
        val id = savedStateHandle.get<Long>(RouteAnalysis::id.name)
            ?: run {
                postSideEffect(AnalysisSideEffect.InvalidAnalysisId)
                return@container
            }

        loadAnalysis(id)
    }

    private fun loadAnalysis(studyRecordId: Long) = intent {
        coroutineScope {
            val analysisDeferred = async { studyRepository.getAnalysis(studyRecordId) }
            val pieChartDeferred =
                async { studyRecordRepository.getPieChart(studyRecordId, "study_time") }

            val analysisResult = analysisDeferred.await()
            val pieChartResult = pieChartDeferred.await()

            val analysis = analysisResult.getOrElse { error ->
                reduce { AnalysisUiState.Error(error.message ?: "분석 결과 로드 실패") }
                return@coroutineScope
            }
            val pieChart = pieChartResult.getOrElse { error ->
                reduce { AnalysisUiState.Error(error.message ?: "Pie Chart 로드 실패") }
                return@coroutineScope
            }

            reduce {
                AnalysisUiState.Data(
                    currentTab = 0,
                    analysis = analysis,
                    pieChart = pieChart
                )
            }
        }
    }

    fun onEvent(event: AnalysisUiEvent) {
        when (event) {
            is AnalysisUiEvent.ChangeTabIndex -> intent {
                val uiState = state as? AnalysisUiState.Data ?: return@intent

                val currentTab = when (event.index) {
                    0 -> "study_time"
                    1 -> "focus"
                    else -> "pose"
                }

                studyRecordRepository.getPieChart(
                    studyRecordId = uiState.analysis.studyRecordId,
                    tab = currentTab
                ).onSuccess { response ->
                    reduce {
                        uiState.copy(
                            currentTab = event.index,
                            pieChart = response
                        )
                    }
                }.onFailure { error ->
                    reduce {
                        AnalysisUiState.Error(error.message ?: "Pie Chart 로드 실패")
                    }
                }
            }
        }
    }
}