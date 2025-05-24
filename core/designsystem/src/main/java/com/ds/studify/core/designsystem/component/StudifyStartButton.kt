package com.ds.studify.core.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.resources.StudifyDrawable
import com.ds.studify.core.resources.StudifyString

@Composable
fun StudifyStartButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(57.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = StudifyColors.PK03
        ),
        contentPadding = PaddingValues(
            vertical = 8.dp, horizontal = 101.dp
        ),
        shape = RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = StudifyDrawable.ic_study_start),
                contentDescription = null,
                modifier = Modifier.size(41.dp)
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = stringResource(id = StudifyString.study_start),
                style = Typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
private fun StudifyStartButtonPreview() {
    StudifyStartButton(
        onClick = {}
    )
}