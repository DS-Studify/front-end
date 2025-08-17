package com.ds.studify.feature.mypage

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.UserRepository
import com.ds.studify.core.resources.StudifyString
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class PasswordChangeUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isPasswordMatch: Boolean = true,
    @StringRes val currentPasswordErrorRes: Int? = null,
    val isLoading: Boolean = false
) {
    val isChangeEnabled: Boolean =
        currentPassword.isNotBlank() &&
                newPassword.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                isPasswordMatch &&
                !isLoading
}

sealed interface PasswordChangeSideEffect {
    data object NavigateBack : PasswordChangeSideEffect
    data class Toast(@StringRes val resId: Int) : PasswordChangeSideEffect
}

@HiltViewModel
class PasswordChangeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel(), ContainerHost<PasswordChangeUiState, PasswordChangeSideEffect> {

    override val container =
        container<PasswordChangeUiState, PasswordChangeSideEffect>(PasswordChangeUiState())

    fun updateCurrentPassword(value: String) = intent {
        reduce { state.copy(currentPassword = value, currentPasswordErrorRes = null) }
    }

    fun updateNewPassword(value: String) = intent {
        val matched = value == state.confirmPassword
        reduce { state.copy(newPassword = value, isPasswordMatch = matched) }
    }

    fun updateConfirmPassword(value: String) = intent {
        val matched = state.newPassword == value
        reduce { state.copy(confirmPassword = value, isPasswordMatch = matched) }
    }

    fun onChangeClick() = intent {
        if (!state.isChangeEnabled) return@intent
        reduce { state.copy(isLoading = true, currentPasswordErrorRes = null) }

        userRepository.patchChangePassword(
            originPassword = state.currentPassword,
            newPassword = state.newPassword
        ).onSuccess {
            reduce { state.copy(isLoading = false) }
            postSideEffect(
                PasswordChangeSideEffect.Toast(
                    StudifyString.mypage_change_success
                )
            )
            postSideEffect(PasswordChangeSideEffect.NavigateBack)
        }.onFailure { e ->
            val isIncorrect = e.message?.contains("INCORRECT_PASSWORD") == true
            reduce {
                state.copy(
                    isLoading = false,
                    currentPasswordErrorRes =
                        if (isIncorrect)
                            StudifyString.mypage_change_failed
                        else
                            StudifyString.auth_password_warning
                )
            }
        }
    }
}