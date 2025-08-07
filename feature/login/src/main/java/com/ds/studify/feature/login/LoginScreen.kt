package com.ds.studify.feature.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.component.StudifySnackBar
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.login.navigation.LoginNavigationDelegator
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun LoginRoute(
    navigationDelegator: LoginNavigationDelegator
) {
    val viewModel: LoginViewModel = hiltViewModel()
    val uiState by viewModel.collectAsState()
    var showLoginFailDialog by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val setLoginFailMessage = stringResource(StudifyString.login_failed_message)

    viewModel.collectSideEffect {
        when (it) {
            is LoginSideEffect.LoginFail -> {
                showLoginFailDialog = true
            }

            is LoginSideEffect.LoginSuccess -> {
                navigationDelegator.onLoginSuccess()
            }
        }
    }

    LaunchedEffect(showLoginFailDialog) {
        if (showLoginFailDialog) {
            scope.launch {
                snackBarHostState.showSnackbar(setLoginFailMessage)
            }
            showLoginFailDialog = false
        }
    }

    when (val state = uiState) {
        is LoginUiState.Login -> {
            LoginScreen(
                navigationDelegator = navigationDelegator,
                uiState = state,
                onEvent = viewModel::onEvent,
                snackBarHostState = snackBarHostState
            )
        }

        is LoginUiState.WaitingLoginResult -> {}
    }
}

@Composable
internal fun LoginScreen(
    navigationDelegator: LoginNavigationDelegator,
    uiState: LoginUiState.Login,
    onEvent: (LoginUiEvent) -> Unit,
    snackBarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StudifyColors.WHITE)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 42.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = StudifyDrawable.app_logo_title_pink),
                contentDescription = null,
                modifier = Modifier.size(width = 200.dp, height = 100.dp)
            )

            Text(
                text = stringResource(id = StudifyString.login_description),
                color = StudifyColors.BLACK,
                fontSize = 16.sp,
                style = Typography.titleSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { onEvent(LoginUiEvent.UpdateEmail(it)) },
                label = {
                    Text(
                        text = stringResource(id = StudifyString.auth_email_label),
                        color = StudifyColors.G03,
                        style = Typography.bodyMedium
                    )
                },
                isError = !uiState.isEmailValid,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (!uiState.isEmailValid) StudifyColors.RED else StudifyColors.PK03,
                    unfocusedBorderColor = if (!uiState.isEmailValid) StudifyColors.RED else StudifyColors.G03,
                    cursorColor = StudifyColors.PK03
                )
            )

            if (!uiState.isEmailValid) {
                Text(
                    text = stringResource(id = StudifyString.auth_email_warning),
                    color = StudifyColors.RED,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }


            OutlinedTextField(
                value = uiState.password,
                onValueChange = { onEvent(LoginUiEvent.UpdatePassword(it)) },
                label = {
                    Text(
                        text = stringResource(id = StudifyString.auth_password_label),
                        color = StudifyColors.G03,
                        style = Typography.bodyMedium
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudifyColors.PK03,
                    unfocusedBorderColor = StudifyColors.G03,
                    cursorColor = StudifyColors.PK03
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = { if (uiState.isEmailValid) onEvent(LoginUiEvent.LoginRequest) },
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudifyColors.PK03),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = stringResource(id = StudifyString.login_button),
                    style = Typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = StudifyColors.G02,
                    thickness = 1.3.dp
                )
                Text(
                    text = stringResource(id = StudifyString.login_no_account),
                    color = StudifyColors.BLACK,
                    style = Typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Text(
                    text = stringResource(id = StudifyString.auth_signup),
                    color = StudifyColors.PK03,
                    style = Typography.bodyMedium,
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .clickable { navigationDelegator.onSignInClicked() }
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = StudifyColors.G02,
                    thickness = 1.3.dp
                )
            }
        }

        StudifySnackBar(
            modifier = Modifier
                .padding(bottom = 60.dp)
                .align(Alignment.BottomCenter),
            hostState = snackBarHostState
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        navigationDelegator = LoginNavigationDelegator(),
        uiState = LoginUiState.Login(),
        onEvent = {},
        snackBarHostState = SnackbarHostState()
    )
}