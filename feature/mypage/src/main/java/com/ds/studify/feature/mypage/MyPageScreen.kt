package com.ds.studify.feature.mypage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.feature.mypage.component.AccountDeletionModal
import com.ds.studify.feature.mypage.navigation.MyPageNavigationDelegator
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
internal fun MyPageRoute(
    onBack: () -> Unit,
    navigationDelegator: MyPageNavigationDelegator
) {
    val viewModel: MyPageViewModel = hiltViewModel()
    val uiState by viewModel.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.refreshProfile()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    viewModel.collectSideEffect {
        when (it) {
            is MyPageSideEffect.LogoutSuccess -> navigationDelegator.onLogoutClick()
            is MyPageSideEffect.AccountDeletionSuccess -> navigationDelegator.onAccountDeletionClick()
            is MyPageSideEffect.Toast -> {
                android.widget.Toast
                    .makeText(
                        context,
                        context.getString(it.resId),
                        android.widget.Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    }

    StudifyScaffoldWithTitle(
        title = stringResource(StudifyString.mypage_title),
        onBackButtonClick = onBack
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = StudifyColors.WHITE)
        ) {
            when (val state = uiState) {
                is MyPageUiState.Loading -> {}

                is MyPageUiState.MyPage -> {
                    MyPageScreen(
                        paddingValues = paddingValues,
                        uiState = state,
                        onEvent = viewModel::onEvent,
                        onPasswordChangeClick = navigationDelegator.onChangePasswordClick,
                        onNicknameChangeClick = navigationDelegator.onChangeNicknameClick
                    )
                }
            }
        }
    }
}

@Composable
internal fun MyPageScreen(
    paddingValues: PaddingValues,
    uiState: MyPageUiState.MyPage,
    onEvent: (MyPageUiEvent) -> Unit,
    onPasswordChangeClick: () -> Unit,
    onNicknameChangeClick: () -> Unit
) {
    var showDeletionModal by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(StudifyColors.WHITE)
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(color = StudifyColors.PK03)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .width(300.dp)
                        .padding(start = 30.dp, top = 30.dp)
                ) {
                    Image(
                        painter = painterResource(id = StudifyDrawable.app_logo_title_color),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .width(150.dp)
                    )
                    Column(
                        verticalArrangement = Arrangement.spacedBy(7.dp),
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text(
                            text = stringResource(StudifyString.mypage_nickname, uiState.userName),
                            color = StudifyColors.WHITE,
                            style = Typography.headlineMedium
                        )
                        Text(
                            text = stringResource(StudifyString.mypage_greetings),
                            color = StudifyColors.WHITE,
                            style = Typography.headlineMedium
                        )
                    }
                    Text(
                        text = uiState.email,
                        color = StudifyColors.WHITE,
                        style = Typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 40.dp)
            ) {
                HorizontalDivider(
                    color = StudifyColors.G03,
                    thickness = 1.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .clickable { onPasswordChangeClick() }
                ) {
                    Text(
                        text = stringResource(StudifyString.mypage_menu_change_password),
                        color = StudifyColors.BLACK,
                        style = Typography.headlineSmall,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = StudifyDrawable.ic_front),
                        contentDescription = null,
                        modifier = Modifier.size(width = 20.dp, height = 15.dp)
                    )
                }
                HorizontalDivider(
                    color = StudifyColors.G03,
                    thickness = 1.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(25.dp)
                        .clickable { onNicknameChangeClick() }
                ) {
                    Text(
                        text = stringResource(StudifyString.mypage_menu_change_nickname),
                        color = StudifyColors.BLACK,
                        style = Typography.headlineSmall,
                        modifier = Modifier.padding(start = 5.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = StudifyDrawable.ic_front),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 20.dp, height = 15.dp)
                    )
                }
                HorizontalDivider(
                    color = StudifyColors.G03,
                    thickness = 1.dp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier
                        .padding(top = 8.dp, start = 5.dp)
                        .clickable { onEvent(MyPageUiEvent.LogoutRequest) }
                ) {
                    Icon(
                        painter = painterResource(id = StudifyDrawable.ic_logout),
                        contentDescription = null,
                        modifier = Modifier.size(width = 20.dp, height = 17.dp)
                    )
                    Text(
                        text = stringResource(StudifyString.mypage_logout),
                        color = StudifyColors.BLACK,
                        style = Typography.headlineSmall
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.8f)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 50.dp)
                        .clickable { showDeletionModal = true }
                ) {
                    Icon(
                        painter = painterResource(id = StudifyDrawable.ic_account_deletion),
                        contentDescription = null,
                        modifier = Modifier.size(width = 22.dp, height = 18.dp)
                    )
                    Text(
                        text = stringResource(StudifyString.mypage_quit),
                        color = StudifyColors.BLACK,
                        style = Typography.headlineSmall,
                    )
                }
            }
        }
        AccountDeletionModal(
            showModal = showDeletionModal,
            onDismiss = { showDeletionModal = false },
            onConfirm = {
                showDeletionModal = false
                onEvent(MyPageUiEvent.AccountDeletionRequest)
            }
        )
    }
}

@Preview
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen(
        paddingValues = PaddingValues(0.dp),
        uiState = MyPageUiState.MyPage(userName = "닉네임", email = "abcd123@gmail.com"),
        onEvent = {},
        onPasswordChangeClick = {},
        onNicknameChangeClick = {}
    )
}