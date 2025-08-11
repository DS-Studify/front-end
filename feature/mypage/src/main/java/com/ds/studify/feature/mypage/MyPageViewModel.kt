package com.ds.studify.feature.mypage

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class MyPage(
        val userName: String
    ) : MyPageUiState
}

sealed interface MyPageUiEvent {
    data object LogoutRequest : MyPageUiEvent
}

sealed interface MyPageSideEffect {
    data object LogoutSuccess : MyPageSideEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel(), ContainerHost<MyPageUiState, MyPageSideEffect> {

    override val container = container<MyPageUiState, MyPageSideEffect>(
        initialState = MyPageUiState.Loading
    ) {
        reduce {
            MyPageUiState.MyPage(userName = "닉네임")
        }
    }

    private fun logout() = intent {
        tokenRepository.clearToken()
        postSideEffect(MyPageSideEffect.LogoutSuccess)
    }

    fun onEvent(event: MyPageUiEvent) {
        when (event) {
            is MyPageUiEvent.LogoutRequest -> {
                logout()
            }
        }
    }
}