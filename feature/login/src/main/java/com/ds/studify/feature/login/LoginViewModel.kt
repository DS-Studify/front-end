package com.ds.studify.feature.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = true
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel(),
    ContainerHost<LoginUiState, Nothing> {

    override val container: Container<LoginUiState, Nothing> = container(LoginUiState())

    private val emailRegex = Regex("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

    fun updateEmail(input: String) = intent {
        val isValid = input.matches(emailRegex)
        reduce { state.copy(email = input, isEmailValid = isValid) }
    }

    fun updatePassword(input: String) = intent {
        reduce { state.copy(password = input) }
    }
}
