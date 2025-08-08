package com.ds.studify.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithLogo
import com.ds.studify.core.designsystem.component.StudifyStartButton
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.designsystem.theme.pretendard
import com.ds.studify.core.domain.entity.HomeEntity
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString
import com.ds.studify.core.ui.extension.formatRecordDuration
import com.ds.studify.feature.home.navigation.HomeNavigationDelegator
import org.orbitmvi.orbit.compose.collectAsState

@Composable
internal fun HomeRoute(
    paddingValues: PaddingValues,
    navigationDelegator: HomeNavigationDelegator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.collectAsState()

    StudifyScaffoldWithLogo(
        paddingValues = paddingValues,
        leftActionButton = {
            IconButton(
                modifier = Modifier.size(width = 28.dp, height = 28.dp),
                onClick = navigationDelegator.onMyPageClick
            ) {
                Icon(
                    painter = painterResource(id = StudifyDrawable.ic_drawer),
                    contentDescription = null,
                    modifier = Modifier.size(width = 20.dp, height = 14.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                    top = 0.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxSize()
                .background(color = StudifyColors.WHITE)
        ) {
            when (val state = uiState) {
                is HomeUiState.Success -> {
                    HomeScreen(
                        uiState = state,
                        navigationDelegator = navigationDelegator
                    )
                }

                is HomeUiState.Loading -> {}

                is HomeUiState.Error -> {}
            }
        }
    }
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState.Success,
    navigationDelegator: HomeNavigationDelegator
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Text(
                    text = uiState.home.nickName,
                    style = Typography.headlineMedium,
                    color = StudifyColors.BLACK
                )
                Text(
                    text = stringResource(id = StudifyString.home_today_study_time),
                    style = Typography.headlineMedium,
                    color = StudifyColors.BLACK
                )
            }

            Text(
                text = formatRecordDuration(uiState.home.todayStudyTime),
                fontFamily = pretendard,
                fontWeight = FontWeight.Bold,
                fontSize = 58.sp,
                color = StudifyColors.PK03
            )
        }

        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 27.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            StudifyStartButton(
                onClick = navigationDelegator.onStartToStudyClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeUiState.Success(
            home = HomeEntity(
                nickName = "닉네임",
                todayStudyTime = 0
            )
        ),
        navigationDelegator = HomeNavigationDelegator()
    )
}