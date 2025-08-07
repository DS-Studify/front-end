package com.ds.studify.feature.login

import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.AuthRepository
import com.ds.studify.core.data.repository.TokenRepository
import com.ds.studify.core.domain.entity.LoginEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

sealed interface LoginUiState {
    data class Login(
        val email: String = "",
        val password: String = "",
        val isEmailValid: Boolean = true
    ) : LoginUiState

    data class WaitingLoginResult(val cachedState: Login) : LoginUiState
}

sealed interface LoginUiEvent {
    data class UpdateEmail(val email: String) : LoginUiEvent
    data class UpdatePassword(val password: String) : LoginUiEvent
    data object LoginRequest : LoginUiEvent
}

sealed interface LoginSideEffect {
    data object LoginSuccess : LoginSideEffect
    data object LoginFail : LoginSideEffect
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) : ViewModel(), ContainerHost<LoginUiState, LoginSideEffect> {

    override val container = container<LoginUiState, LoginSideEffect>(LoginUiState.Login())

    private val emailRegex =
        Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    private fun loginToServer() = intent {
        val loginState = state as? LoginUiState.Login ?: return@intent

        reduce {
            LoginUiState.WaitingLoginResult(loginState)
        }

        authRepository.postLogin(
            loginData = LoginEntity(
                email = loginState.email,
                password = loginState.password
            )
        )
            .onSuccess { response ->
                tokenRepository.setAccessToken(response.accessToken, response.refreshToken)
                postSideEffect(LoginSideEffect.LoginSuccess)
            }
            .onFailure {
                postSideEffect(LoginSideEffect.LoginFail)
                reduce {
                    (state as? LoginUiState.WaitingLoginResult)?.cachedState ?: LoginUiState.Login()
                }
            }
    }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UpdateEmail -> blockingIntent {
                val loginState = state as? LoginUiState.Login ?: return@blockingIntent
                val isValid = event.email.matches(emailRegex)

                reduce {
                    loginState.copy(email = event.email, isEmailValid = isValid)
                }
            }

            is LoginUiEvent.UpdatePassword -> blockingIntent {
                val loginState = state as? LoginUiState.Login ?: return@blockingIntent
                reduce {
                    loginState.copy(password = event.password)
                }
            }

            is LoginUiEvent.LoginRequest -> {
                loginToServer()
            }
        }
    }
}
