package com.ds.studify.feature.mypage

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.ds.studify.core.resources.StudifyString
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class NicknameChangeUiState(
    val nickname: String = "",
    @StringRes val nicknameErrorRes: Int? = null,
    val isLoading: Boolean = false
) {
    val isChangeEnabled: Boolean = nickname.isNotBlank() && !isLoading
}

sealed interface NicknameChangeSideEffect {
    data object NavigateBack : NicknameChangeSideEffect
}

@HiltViewModel
class NicknameChangeViewModel @Inject constructor(
) : ViewModel(), ContainerHost<NicknameChangeUiState, NicknameChangeSideEffect> {

    override val container = container<NicknameChangeUiState, NicknameChangeSideEffect>(
        NicknameChangeUiState()
    )

    fun updateNickname(value: String) = intent {
        val err = if (value.isBlank()) StudifyString.auth_nickname_warning else null
        reduce { state.copy(nickname = value, nicknameErrorRes = err) }
    }

    fun onChangeClick() = intent {
        if (state.nickname.isBlank()) {
            reduce { state.copy(nicknameErrorRes = StudifyString.auth_nickname_warning) }
            return@intent
        }
    }
}