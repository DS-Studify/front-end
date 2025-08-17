package com.ds.studify.feature.mypage

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
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
}

@HiltViewModel
class PasswordChangeViewModel @Inject constructor(
) : ViewModel(), ContainerHost<PasswordChangeUiState, PasswordChangeSideEffect> {

    override val container = container<PasswordChangeUiState, PasswordChangeSideEffect>(PasswordChangeUiState())

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

    fun onChangeClick() = intent {}
}