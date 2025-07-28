package com.ds.studify.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.login.navigation.LoginNavigationDelegator
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun LoginRoute(
    paddingValues: PaddingValues,
    navigationDelegator: LoginNavigationDelegator,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    LoginScreen(
        paddingValues = paddingValues,
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onLoginClick = navigationDelegator.onLoginClick
    )
}

@Composable
internal fun LoginScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    uiState: LoginUiState = LoginUiState(),
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = 0.dp,
                bottom = paddingValues.calculateBottomPadding()
            )
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
                onValueChange = onEmailChange,
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
                onValueChange = onPasswordChange,
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
                onClick = { if (uiState.isEmailValid) onLoginClick() },
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
                        .clickable {}
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = StudifyColors.G02,
                    thickness = 1.3.dp
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen()
}
