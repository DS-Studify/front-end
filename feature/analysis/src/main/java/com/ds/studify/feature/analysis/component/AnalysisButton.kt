package com.ds.studify.feature.analysis.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography

@Composable
fun AnalysisPrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(42.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = StudifyColors.PK03
        ),
        contentPadding = PaddingValues(horizontal = 30.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            style = Typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AnalysisOutlinedButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(42.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFDDDDDD),
        ),
        contentPadding = PaddingValues(horizontal = 30.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF858585),
            style = Typography.headlineSmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun AnalysisPrimaryButtonPreview() {
    AnalysisPrimaryButton(
        text = "종료하기",
        onClick = {}
    )
}

@Preview
@Composable
private fun AnalysisOutlinedButtonPreview() {
    AnalysisOutlinedButton(
        text = "다시 공부하기",
        onClick = {}
    )
}