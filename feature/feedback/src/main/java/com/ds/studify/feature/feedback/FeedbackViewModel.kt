package com.ds.studify.feature.feedback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.FeedbackEntity
import com.ds.studify.core.domain.entity.PieChartEntity
import com.ds.studify.feature.feedback.navigation.RouteFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface FeedbackState {
    data object Loading : FeedbackState
    data class Success(
        val currentTab: Int,
        val feedback: FeedbackEntity,
        val pieChart: List<PieChartEntity>
    ) : FeedbackState

    data class Error(val message: String) : FeedbackState
}

sealed interface FeedbackUiEvent {
    data class ChangeTabIndex(val index: Int) : FeedbackUiEvent
}

sealed interface FeedbackSideEffect {
    data object InvalidFeedbackId : FeedbackSideEffect
}

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val studyRecordRepository: StudyRecordRepository
) : ViewModel(), ContainerHost<FeedbackState, FeedbackSideEffect> {

    override val container = container<FeedbackState, FeedbackSideEffect>(
        initialState = FeedbackState.Loading
    ) {
        val id = savedStateHandle.get<Long>(RouteFeedback::id.name)
            ?: run {
                postSideEffect(FeedbackSideEffect.InvalidFeedbackId)
                return@container
            }

        loadFeedback(id)
    }

    private fun loadFeedback(id: Long) = intent {
        coroutineScope {
            val feedbackDeferred = async { studyRecordRepository.getFeedback(id) }
            val pieChartDeferred = async { studyRecordRepository.getPieChart(id, "study_time") }

            val feedbackResult = feedbackDeferred.await()
            val pieChartResult = pieChartDeferred.await()

            val feedback = feedbackResult.getOrElse { error ->
                reduce { FeedbackState.Error(error.message ?: "피드백 상세 데이터 로드 실패") }
                return@coroutineScope
            }
            val pieChart = pieChartResult.getOrElse { error ->
                reduce { FeedbackState.Error(error.message ?: "Pie Chart 로드 실패") }
                return@coroutineScope
            }

            reduce {
                FeedbackState.Success(
                    currentTab = 0,
                    feedback = feedback,
                    pieChart = pieChart
                )
            }
        }
    }

    fun onEvent(event: FeedbackUiEvent) {
        when (event) {
            is FeedbackUiEvent.ChangeTabIndex -> intent {
                val uiState = state as? FeedbackState.Success ?: return@intent

                val currentTab = when (event.index) {
                    0 -> "study_time"
                    1 -> "focus"
                    else -> "pose"
                }

                studyRecordRepository.getPieChart(
                    studyRecordId = uiState.feedback.studyRecordId,
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
                        FeedbackState.Error(error.message ?: "Pie Chart 로드 실패")
                    }
                }
            }
        }
    }
}