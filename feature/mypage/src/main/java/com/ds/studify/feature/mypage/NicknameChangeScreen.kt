package com.ds.studify.feature.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyString
import org.orbitmvi.orbit.compose.collectAsState
import kotlin.math.max

@Composable
internal fun NicknameChangeRoute(
    onBack: () -> Unit,
    viewModel: NicknameChangeViewModel = hiltViewModel()
) {
    val uiState = viewModel.collectAsState().value

    StudifyScaffoldWithTitle(
        title = stringResource(id = StudifyString.mypage_edit_profile_title),
        onBackButtonClick = onBack
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(StudifyColors.WHITE)
        ) {
            NicknameChangeScreen(
                paddingValues = paddingValues,
                uiState = uiState,
                updateNickname = viewModel::updateNickname,
                onChangeClick = { viewModel.onChangeClick() }
            )
        }
    }
}

@Composable
internal fun NicknameChangeScreen(
    paddingValues: PaddingValues,
    uiState: NicknameChangeUiState,
    updateNickname: (String) -> Unit,
    onChangeClick: () -> Unit
) {
    val density = LocalDensity.current
    val imeBottomPx = WindowInsets.ime.getBottom(density)
    val isImeVisible = imeBottomPx > 0
    val extraScrollPadding = if (isImeVisible) max(24, (imeBottomPx * 0.25f).toInt()) else 0
    val extraScrollPaddingDp = with(density) { extraScrollPadding.toDp() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(StudifyColors.WHITE)
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = 0.dp,
                bottom = paddingValues.calculateBottomPadding()
            )
            .imePadding()
            .padding(horizontal = 42.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(24.dp + extraScrollPaddingDp))

        Text(
            text = stringResource(id = StudifyString.auth_nickname_label),
            style = Typography.headlineSmall,
            color = StudifyColors.BLACK
        )

        Spacer(modifier = Modifier.height(8.dp))

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(12.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onChangeClick,
            shape = RoundedCornerShape(4.dp),
            enabled = uiState.isChangeEnabled,
            colors = ButtonDefaults.buttonColors(containerColor = StudifyColors.PK03),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = stringResource(id = StudifyString.mypage_change),
                style = Typography.titleSmall
            )
        }

        Spacer(modifier = Modifier.height(24.dp + extraScrollPaddingDp))
    }
}

@Preview
@Composable
private fun NicknameChangeScreenPreview() {
    NicknameChangeScreen(
        paddingValues = PaddingValues(0.dp),
        uiState = NicknameChangeUiState(),
        updateNickname = {},
        onChangeClick = {}
    )
}
