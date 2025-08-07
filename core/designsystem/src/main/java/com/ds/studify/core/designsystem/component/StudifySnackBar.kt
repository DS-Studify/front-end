package com.ds.studify.core.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography

@Composable
fun StudifySnackBar(
    modifier: Modifier = Modifier,
    hostState: SnackbarHostState
) {
    SnackbarHost(
        modifier = modifier,
        hostState = hostState
    ) {
        StudifySnackBarContent(message = it.visuals.message)
    }
}

@Composable
private fun StudifySnackBarContent(
    message: String
) {
    Surface(
        modifier = Modifier
            .width(330.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(12.dp),
        color = StudifyColors.G04
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 14.dp)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = message,
                style = Typography.labelMedium,
                color = StudifyColors.WHITE,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StudifySnackBarPreview() {
    StudifySnackBarContent(
        message = "아이디 또는 비밀번호를 다시 확인해 주세요."
    )
}