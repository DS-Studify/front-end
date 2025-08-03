package com.ds.studify.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString

@Composable
fun StudifyTitleBar(
    title: String,
    onBackButtonClick: (() -> Unit)? = null,
    @DrawableRes navIcon: Int = StudifyDrawable.ic_back,
    color: Color = StudifyColors.BLACK
) {
    Box(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .height(APP_LOGO_TITLE_HEIGHT_DP)
    ) {
        onBackButtonClick?.let {
            IconButton(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 2.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = color
                ),
                onClick = onBackButtonClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(width = 11.dp, height = 20.dp),
                    painter = painterResource(id = navIcon),
                    contentDescription = stringResource(StudifyString.back)
                )
            }
        }
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = title,
            color = color,
            style = Typography.headlineSmall
        )
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 56)
@Composable
private fun StudifyTitleBarPreview() {
    StudifyTitleBar(
        title = stringResource(StudifyString.studify),
        onBackButtonClick = {}
    )
}