package com.ds.studify.feature.feedback

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ds.studify.feature.feedback.navigation.RouteFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface FeedbackState {
    data object Loading : FeedbackState
    data class Screen(
        val currentTab: Int,
        val studyDate: String
    ) : FeedbackState
}

sealed interface FeedbackUiEvent {
    data class ChangeTabIndex(val index: Int) : FeedbackUiEvent
}

sealed interface FeedbackSideEffect {
    data object InvalidFeedbackId : FeedbackSideEffect
    data object InvalidFeedback : FeedbackSideEffect
}

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ContainerHost<FeedbackState, FeedbackSideEffect> {

    override val container = container<FeedbackState, FeedbackSideEffect>(
        initialState = FeedbackState.Loading
    ) {
        val id = savedStateHandle.get<Long>(RouteFeedback::id.name)
            ?: run {
                postSideEffect(FeedbackSideEffect.InvalidFeedbackId)
                return@container
            }
        reduce {
            FeedbackState.Screen(
                currentTab = 0,
                studyDate = "2025년 8월 3일"
            )
        }
    }

    fun onEvent(event: FeedbackUiEvent) {
        when (event) {
            is FeedbackUiEvent.ChangeTabIndex -> intent {
                val uiState = state as? FeedbackState.Screen ?: return@intent
                reduce {
                    uiState.copy(
                        currentTab = event.index
                    )
                }
            }
        }
    }
}