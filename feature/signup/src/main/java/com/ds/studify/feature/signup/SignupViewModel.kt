package com.ds.studify.feature.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class SignupUiState(
    val email: String = "",
    val isEmailValid: Boolean = true,
    val verificationCode: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordMatch: Boolean = true,
    val nickname: String = ""
)


@HiltViewModel
class SignupViewModel @Inject constructor() :
    ViewModel(),
    ContainerHost<SignupUiState, Nothing> {

    override val container = container<SignupUiState, Nothing>(SignupUiState())

    fun updateEmail(email: String) = intent {
        val isValid = email.matches(Regex(EMAIL_REGEX))
        reduce { state.copy(email = email, isEmailValid = isValid) }
    }

    fun updateVerificationCode(code: String) = intent {
        reduce { state.copy(verificationCode = code) }
    }

    fun updatePassword(password: String) = intent {
        val isMatched = password == state.confirmPassword
        reduce {
            state.copy(
                password = password,
                isPasswordMatch = isMatched
            )
        }
    }

    fun updateConfirmPassword(confirm: String) = intent {
        val isMatched = state.password == confirm
        reduce { state.copy(confirmPassword = confirm, isPasswordMatch = isMatched) }
    }


    fun updateNickname(nickname: String) = intent {
        reduce { state.copy(nickname = nickname) }
    }

    companion object {
        private const val EMAIL_REGEX =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}
