package com.ds.studify.feature.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.signup.navigation.TermsType
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.math.max

@Composable
internal fun SignupRoute(
    onNavigateLogin: () -> Unit,
    onNavigateTerms: (TermsType) -> Unit,
    viewModel: SignupViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    viewModel.collectSideEffect { effect ->
        when (effect) {
            is SignupSideEffect.Toast ->
                Toast.makeText(context, context.getString(effect.resId), Toast.LENGTH_SHORT).show()

            SignupSideEffect.NavigateLogin ->
                onNavigateLogin()
        }
    }

    SignupScreen(
        uiState = uiState,
        updateEmail = viewModel::updateEmail,
        updateVerificationCode = viewModel::updateVerificationCode,
        updatePassword = viewModel::updatePassword,
        updateConfirmPassword = viewModel::updateConfirmPassword,
        updateNickname = viewModel::updateNickname,
        onAgreementChanged = viewModel::onAgreementChanged,
        onAllAgreementsChanged = viewModel::onAllAgreementsChanged,
        onSendVerificationClick = {
//            if (uiState.showTimer) {
//                viewModel.reverify()
//            } else
                viewModel.sendVerification()
        },
        onNavigateTerms = onNavigateTerms,
        onSignupClick = { viewModel.onSignupClick() }
    )

}

@Composable
internal fun SignupScreen(
    paddingValues: PaddingValues = PaddingValues(0.dp),
    uiState: SignupUiState = SignupUiState(),
    updateEmail: (String) -> Unit = {},
    updateVerificationCode: (String) -> Unit = {},
    updatePassword: (String) -> Unit = {},
    updateConfirmPassword: (String) -> Unit = {},
    updateNickname: (String) -> Unit = {},
    onAgreementChanged: (Boolean?, Boolean?, Boolean?) -> Unit = { _, _, _ -> },
    onAllAgreementsChanged: (Boolean) -> Unit = {},
    onSendVerificationClick: () -> Unit = {},
    onNavigateTerms: (TermsType) -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    val isImeVisible = imeBottomPx > 0
    val extraScrollPadding = if (isImeVisible) {
        max(24, (imeBottomPx * 0.25f).toInt())
    } else 0
    val extraScrollPaddingDp = with(density) { extraScrollPadding.toDp() }

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
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 42.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(24.dp + extraScrollPaddingDp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = StudifyDrawable.app_logo_title),
                    contentDescription = null,
                    modifier = Modifier.size(width = 80.dp, height = 40.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = stringResource(id = StudifyString.signup_title_1),
                    style = Typography.titleLarge,
                    color = StudifyColors.BLACK
                )
            }

            Text(
                text = stringResource(id = StudifyString.signup_title_2),
                color = StudifyColors.BLACK,
                style = Typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = updateEmail,
                label = {
                    Text(
                        text = stringResource(id = StudifyString.auth_email_label),
                        color = StudifyColors.G03,
                        style = Typography.bodyMedium
                    )
                },
                isError = (!uiState.isEmailValid) || (uiState.emailErrorRes != null),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if ((!uiState.isEmailValid) || (uiState.emailErrorRes != null)) StudifyColors.RED else StudifyColors.PK03,
                    unfocusedBorderColor = if ((!uiState.isEmailValid) || (uiState.emailErrorRes != null)) StudifyColors.RED else StudifyColors.G03,
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
            } else if (uiState.emailErrorRes != null) {
                Text(
                    text = stringResource(id = uiState.emailErrorRes),
                    color = StudifyColors.RED,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.showTimer) {
                    Text(
                        text = uiState.timerText,
                        style = Typography.bodyMedium,
                        color = StudifyColors.G03,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                Button(
                    onClick = onSendVerificationClick,
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = StudifyColors.PK03),
                    enabled = uiState.isEmailValid && !uiState.isLoading,
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Text(
                        text = if (uiState.showTimer) stringResource(id = StudifyString.signup_verification_code_resend_button)
                        else stringResource(id = StudifyString.signup_verification_code_button),
                        style = Typography.bodyMedium
                    )
                }
            }

            OutlinedTextField(
                value = uiState.verificationCode,
                onValueChange = updateVerificationCode,
                label = {
                    Text(
                        text = stringResource(id = StudifyString.signup_verification_code_label),
                        color = StudifyColors.G03,
                        style = Typography.bodyMedium
                    )
                },
                isError = uiState.verificationErrorRes != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (uiState.verificationErrorRes != null) StudifyColors.RED else StudifyColors.PK03,
                    unfocusedBorderColor = if (uiState.verificationErrorRes != null) StudifyColors.RED else StudifyColors.G03,
                    cursorColor = StudifyColors.PK03
                )
            )

            if (uiState.verificationErrorRes != null) {
                Text(
                    text = stringResource(id = uiState.verificationErrorRes),
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
                onValueChange = updatePassword,
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
                    focusedBorderColor = if (!uiState.isPasswordMatch) StudifyColors.RED else StudifyColors.PK03,
                    unfocusedBorderColor = if (!uiState.isPasswordMatch) StudifyColors.RED else StudifyColors.G03,
                    cursorColor = StudifyColors.PK03
                ),
                visualTransformation = PasswordVisualTransformation()
            )

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = updateConfirmPassword,
                label = {
                    Text(
                        text = stringResource(id = StudifyString.auth_password_confirm_label),
                        color = StudifyColors.G03,
                        style = Typography.bodyMedium
                    )
                },
                isError = !uiState.isPasswordMatch,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (!uiState.isPasswordMatch) StudifyColors.RED else StudifyColors.PK03,
                    unfocusedBorderColor = if (!uiState.isPasswordMatch) StudifyColors.RED else StudifyColors.G03,
                    cursorColor = StudifyColors.PK03
                )
            )

            if (!uiState.isPasswordMatch) {
                Text(
                    text = stringResource(id = StudifyString.auth_password_warning),
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
                value = uiState.nickname,
                onValueChange = updateNickname,
                label = {
                    Text(
                        text = stringResource(id = StudifyString.auth_nickname_label),
                        color = StudifyColors.G03,
                        style = Typography.bodyMedium
                    )
                },
                isError = uiState.nicknameErrorRes != null,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (uiState.nicknameErrorRes != null) StudifyColors.RED else StudifyColors.PK03,
                    unfocusedBorderColor = if (uiState.nicknameErrorRes != null) StudifyColors.RED else StudifyColors.G03,
                    cursorColor = StudifyColors.PK03
                )
            )

            if (uiState.nicknameErrorRes != null) {
                Text(
                    text = stringResource(id = uiState.nicknameErrorRes),
                    color = StudifyColors.RED,
                    style = Typography.bodySmall,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(12.dp))
            }

            TermsAgreementSection(
                uiState = uiState,
                onAllAgreementsChanged = onAllAgreementsChanged,
                onAgreementChanged = onAgreementChanged,
                onNavigateTerms = onNavigateTerms
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSignupClick,
                shape = RoundedCornerShape(4.dp),
                colors = ButtonDefaults.buttonColors(containerColor = StudifyColors.PK03),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = uiState.isSignupEnabled && !uiState.isLoading
            ) {
                Text(
                    text = stringResource(id = StudifyString.auth_signup),
                    style = Typography.titleSmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp + extraScrollPaddingDp))
        }
    }
}

// 약관 전체
@Composable
private fun TermsAgreementSection(
    uiState: SignupUiState,
    onAllAgreementsChanged: (Boolean) -> Unit,
    onAgreementChanged: (Boolean?, Boolean?, Boolean?) -> Unit,
    onNavigateTerms: (TermsType) -> Unit
) {
    val areAllAgreed = uiState.isServiceTermsAgreed && uiState.isPrivacyPolicyAgreed && uiState.isAgeConfirmed

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .clickable { onAllAgreementsChanged(!areAllAgreed) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = areAllAgreed,
                onCheckedChange = { onAllAgreementsChanged(it) },
                colors = CheckboxDefaults.colors(checkedColor = StudifyColors.PK03),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = StudifyString.signup_terms_agree_all),
                style = Typography.bodyMedium
            )
        }
        AgreementItem(
            checked = uiState.isServiceTermsAgreed,
            onCheckedChange = { onAgreementChanged(it, null, null) },
            text = stringResource(id = StudifyString.signup_terms_service),
            onViewClick = { onNavigateTerms(TermsType.SERVICE) }
        )
        AgreementItem(
            checked = uiState.isPrivacyPolicyAgreed,
            onCheckedChange = { onAgreementChanged(null, it, null) },
            text = stringResource(id = StudifyString.signup_terms_privacy),
            onViewClick = { onNavigateTerms(TermsType.PRIVACY) }
        )
        AgreementItem(
            checked = uiState.isAgeConfirmed,
            onCheckedChange = { onAgreementChanged(null, null, it) },
            text = stringResource(id = StudifyString.signup_terms_age),
            onViewClick = null
        )
    }
}

// 각 약관 항목
@Composable
private fun AgreementItem(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String,
    onViewClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = StudifyColors.PK03),
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = Typography.bodySmall, modifier = Modifier.weight(1f))

        val viewClick = onViewClick
        if (viewClick != null) {
            Text(
                text = stringResource(id = StudifyString.signup_terms_view),
                style = Typography.bodySmall,
                color = StudifyColors.G03,
                modifier = Modifier
                    .clickable { viewClick() }
                    .padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SignupScreenPreview() {
    SignupScreen()
}
