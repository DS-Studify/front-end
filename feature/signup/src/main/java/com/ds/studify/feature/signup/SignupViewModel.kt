package com.ds.studify.feature.signup

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.studify.core.data.repository.AuthRepository
import com.ds.studify.core.domain.entity.SignupEntity
import com.ds.studify.core.resources.StudifyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class SignupUiState(
    val email: String = "",
    val isEmailValid: Boolean = false,
    @StringRes
    val emailErrorRes: Int? = null,

    val verificationCode: String = "",
    @StringRes
    val verificationErrorRes: Int? = null,

    val password: String = "",
    val confirmPassword: String = "",
    val isPasswordMatch: Boolean = true,

    val nickname: String = "",
    @StringRes
    val nicknameErrorRes: Int? = null,

    val isLoading: Boolean = false,

    val resendSecondsLeft: Int = 0,
    val registerSuccess: Boolean = false
){
    val isSignupEnabled: Boolean =
        isEmailValid &&
        isPasswordMatch &&
        email.isNotBlank() &&
        verificationCode.isNotBlank() &&
        password.isNotBlank() &&
        confirmPassword.isNotBlank() &&
        nickname.isNotBlank()

    // 타이머
    val showTimer: Boolean = resendSecondsLeft > 0
    val timerText: String
        get() = "%d:%02d".format(resendSecondsLeft / 60, resendSecondsLeft % 60)
}

sealed interface SignupSideEffect {
    data class Toast(@StringRes val resId: Int) : SignupSideEffect
    data object NavigateLogin : SignupSideEffect
}

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel(), ContainerHost<SignupUiState, SignupSideEffect> {

    override val container = container<SignupUiState, SignupSideEffect>(SignupUiState())

    private var timerJob: Job? = null

    fun updateEmail(email: String) = intent {
        val isValid = email.matches(Regex(EMAIL_REGEX))
        timerJob?.cancel()
        reduce {
            state.copy(
                email = email,
                isEmailValid = isValid,
                emailErrorRes = null,
                verificationErrorRes = null,
                verificationCode = "",
                resendSecondsLeft = 0
            )
        }
    }

    fun updateVerificationCode(code: String) = intent {
        reduce { state.copy(verificationCode = code, verificationErrorRes = null) }
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
        val error = if (nickname.isBlank()) StudifyString.auth_nickname_warning else null
        reduce { state.copy(nickname = nickname) }
    }

    // 인증번호 전송
    fun sendVerification() = intent {
        if (!state.isEmailValid) return@intent
        reduce { state.copy(isLoading = true) }

        authRepository.postSendVerification(state.email)
            .onSuccess {
                reduce { state.copy(verificationErrorRes = null, verificationCode = "") }
                startTimer(300)
                postSideEffect(SignupSideEffect.Toast(StudifyString.signup_verification_code_sent_message))
            }
            .onFailure {
                postSideEffect(SignupSideEffect.Toast(StudifyString.signup_verification_code_failed_message))
            }
        reduce { state.copy(isLoading = false) }
    }

    // 인증번호 재전송
    fun reverify() = intent {
        if (!state.isEmailValid) return@intent
        reduce { state.copy(isLoading = true) }
        authRepository.postReverify(state.email)
            .onSuccess {
                reduce { state.copy(verificationErrorRes = null, verificationCode = "") }
                startTimer(300)
                postSideEffect(SignupSideEffect.Toast(StudifyString.signup_verification_code_resent_message))
            }
            .onFailure {
                postSideEffect(SignupSideEffect.Toast(StudifyString.signup_verification_code_failed_message))
            }
        reduce { state.copy(isLoading = false) }
    }

    // 회원가입
    fun onSignupClick() = intent {
        if (!state.isSignupEnabled) return@intent
        reduce { state.copy(isLoading = true, emailErrorRes = null, verificationErrorRes = null) }

        val verified = authRepository.postCheckVerification(state.email, state.verificationCode)
            .getOrElse {
                reduce { state.copy(isLoading = false, verificationErrorRes = StudifyString.signup_verification_code_warning) }
                return@intent
            }
        if (!verified) {
            reduce { state.copy(isLoading = false, verificationErrorRes = StudifyString.signup_verification_code_warning) }
            return@intent
        }

        val entity = SignupEntity(
            email = state.email,
            password = state.password,
            nickname = state.nickname
        )
        val result = authRepository.postRegister(entity)

        if (result.isFailure) {
            val msg = result.exceptionOrNull()?.message.orEmpty()
            if (msg.contains("이미 존재하는 이메일")) {
                reduce { state.copy(isLoading = false, emailErrorRes = StudifyString.signup_duplicate_email) }
            } else {
                reduce { state.copy(isLoading = false, emailErrorRes = StudifyString.signup_register_failed) }
            }
            return@intent
        }

        timerJob?.cancel()
        reduce { state.copy(isLoading = false, registerSuccess = true) }
        postSideEffect(SignupSideEffect.NavigateLogin)
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            for (s in seconds downTo 1) {
                intent { reduce { state.copy(resendSecondsLeft = s) } }
                delay(1_000)
            }
            intent {
                reduce {
                    state.copy(
                        resendSecondsLeft = 0,
                        verificationErrorRes = StudifyString.signup_verification_code_expired
                    )
                }
            }
        }
    }

    companion object {
        private const val EMAIL_REGEX =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
    }
}
