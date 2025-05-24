package com.ds.studify.feature.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.component.StudifyScaffoldWithTitle
import com.ds.studify.core.designsystem.component.StudifyStartButton
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString

@Composable
internal fun CameraGuideRoute(
    onBack: () -> Unit,
    onNavigateToStudy: () -> Unit
) {
    StudifyScaffoldWithTitle(
        titleId = StudifyString.camera_guide_title,
        onBackButtonClick = onBack
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CameraGuideScreen(
                paddingValues = paddingValues,
                onNavigateToStudy = onNavigateToStudy
            )
        }
    }
}

@Composable
internal fun CameraGuideScreen(
    paddingValues: PaddingValues,
    onNavigateToStudy: () -> Unit
) {
    val scrollState = rememberScrollState()

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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                CameraGuideContent()
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 10.dp, bottom = 21.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StudifyStartButton(
                    onClick = onNavigateToStudy
                )
            }
        }
    }
}

@Composable
private fun CameraGuideContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp)
            .padding(horizontal = 30.dp)
    ) {
        Text(
            text = stringResource(id = StudifyString.camera_guide_setting_title),
            style = Typography.headlineMedium,
            color = StudifyColors.BLACK
        )

        Image(
            painter = painterResource(id = StudifyDrawable.image_camera_guide_sample),
            contentDescription = null,
            modifier = Modifier
                .padding(vertical = 15.dp)
                .size(width = 297.dp, height = 181.dp)
                .align(Alignment.CenterHorizontally)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "1.",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK
                )
                Text(
                    text = buildAnnotatedString {
                        append(stringResource(id = StudifyString.camera_guide_setting_angle_title))
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(id = StudifyString.camera_guide_setting_angle_body_and_hand))
                        }
                        append(stringResource(id = StudifyString.camera_guide_setting_angle_content1))
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(id = StudifyString.camera_guide_setting_angle_angle))
                        }
                        append(stringResource(id = StudifyString.camera_guide_setting_angle_content2))
                    },
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "2.",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK
                )
                Text(
                    text = stringResource(id = StudifyString.camera_guide_setting_distance),
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "3.",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK
                )
                Text(
                    text = stringResource(id = StudifyString.camera_guide_setting_light),
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "4.",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK
                )
                Text(
                    text = stringResource(id = StudifyString.camera_guide_setting_background),
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "5.",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK
                )
                Text(
                    text = stringResource(id = StudifyString.camera_guide_setting_fix),
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }
        }

        Spacer(modifier = Modifier.size(26.dp))

        Text(
            text = stringResource(id = StudifyString.camera_guide_precautions_title),
            style = Typography.headlineMedium
        )

        Column(
            modifier = Modifier
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "＊",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = stringResource(id = StudifyString.camera_guide_precautions_content_1),
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "＊",
                    style = Typography.headlineSmall,
                    color = StudifyColors.BLACK,
                    modifier = Modifier.padding(top = 2.dp)
                )
                Text(
                    text = stringResource(id = StudifyString.camera_guide_precautions_content_2),
                    style = Typography.bodyMedium,
                    color = StudifyColors.BLACK
                )
            }
        }

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(id = StudifyString.camera_guide_done_content_1))
                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(stringResource(id = StudifyString.camera_guide_done_content_2))
                }
                append(" ")
                append(stringResource(id = StudifyString.camera_guide_done_content_3))
            },
            style = Typography.bodyLarge,
            color = StudifyColors.BLACK
        )
    }
}

@Preview
@Composable
private fun CameraGuideScreenPreview() {
    CameraGuideScreen(
        paddingValues = PaddingValues(0.dp),
        onNavigateToStudy = {}
    )
}