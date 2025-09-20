package com.ds.studify.feature.mypage

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.TokenRepository
import com.ds.studify.core.data.repository.UserRepository
import com.ds.studify.core.resources.StudifyString
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface MyPageUiState {
    data object Loading : MyPageUiState
    data class MyPage(
        val userName: String,
        val email: String
    ) : MyPageUiState
}

sealed interface MyPageUiEvent {
    data object LogoutRequest : MyPageUiEvent
    data object AccountDeletionRequest : MyPageUiEvent
}

sealed interface MyPageSideEffect {
    data object LogoutSuccess : MyPageSideEffect
    data object AccountDeletionSuccess : MyPageSideEffect
    data class Toast(@androidx.annotation.StringRes val resId: Int) : MyPageSideEffect
}

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val userRepository: UserRepository
) : ViewModel(), ContainerHost<MyPageUiState, MyPageSideEffect> {

    override val container = container<MyPageUiState, MyPageSideEffect>(
        initialState = MyPageUiState.Loading
    ) {
        refreshProfile()
    }

    fun refreshProfile() = intent {
        val result = userRepository.getProfile()
        result.onSuccess { profile ->
            reduce { MyPageUiState.MyPage(userName = profile.nickname, email = profile.email) }
        }.onFailure {
            reduce {
                when (val s = state) {
                    is MyPageUiState.MyPage -> s
                    else -> MyPageUiState.MyPage(userName = "닉네임", email = "")
                }
            }
            postSideEffect(MyPageSideEffect.Toast(StudifyString.mypage_profile_load_failed))
        }
    }

    private fun logout() = intent {
        tokenRepository.clearToken()
        postSideEffect(MyPageSideEffect.LogoutSuccess)
    }

    private fun deleteUser() = intent {
        val result = userRepository.deleteUser()
        result.onSuccess {
            tokenRepository.clearToken()
            postSideEffect(MyPageSideEffect.AccountDeletionSuccess)
        }. onFailure {
            postSideEffect(MyPageSideEffect.Toast(StudifyString.mypage_account_deletion_failed))
        }
    }

    fun onEvent(event: MyPageUiEvent) {
        when (event) {
            is MyPageUiEvent.LogoutRequest -> {
                logout()
            }
            is MyPageUiEvent.AccountDeletionRequest -> deleteUser()
        }
    }


}