package com.ds.studify.core.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.resources.StudifyString

@Composable
fun DescriptionDialog(
    description: String,
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit = onDismiss
) {
    if (showDialog) {
        AlertDialog(
            containerColor = StudifyColors.WHITE,
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = StudifyColors.G03
                    )
                ) {
                    Text(
                        text = stringResource(StudifyString.confirm),
                        color = StudifyColors.PK03
                    )
                }
            },
            text = {
                Text(
                    text = description,
                    color = StudifyColors.BLACK
                )
            }
        )
    }
}

@Preview
@Composable
private fun StudifyDescriptionDialogPreview() {
    DescriptionDialog(
        description = "*공부 시간 중 집중 비율",
        showDialog = true,
        onDismiss = {}
    )
}