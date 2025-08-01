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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString

@Composable
internal fun MyPageRoute(
    onBack: () -> Unit
) {
    StudifyScaffoldWithTitle(
        titleId = StudifyString.mypage_title,
        onBackButtonClick = onBack
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MyPageScreen(
                paddingValues = paddingValues
            )
        }
    }
}

@Composable
internal fun MyPageScreen(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = StudifyColors.WHITE)
            .padding(
                start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(top = 10.dp)
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
                            text = "닉네임님",
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
                        text = "abcd123@gmail.com",
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
                        .clickable {}
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
                        .clickable {}
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
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .padding(top = 8.dp, start = 5.dp)
                        .clickable {}
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
        }

    }

}

@Preview
@Composable
private fun MyPageScreenPreview() {
    MyPageScreen(
        paddingValues = PaddingValues(0.dp)
    )
}