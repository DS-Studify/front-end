package com.ds.studify.feature.mypage.component

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
fun AccountDeletionModal(
    showModal: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if(showModal) {
        AlertDialog(
            containerColor = StudifyColors.WHITE,
            onDismissRequest = onDismiss,
            dismissButton = {
                TextButton(
                    onClick = onDismiss,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = StudifyColors.G03
                    )
                ) {
                    Text(
                        text = stringResource(StudifyString.mypage_cancel),
                        color = StudifyColors.G03
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirm,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = StudifyColors.G03
                    )
                ) {
                    Text(
                        text = stringResource(StudifyString.mypage_quit),
                        color = StudifyColors.RED02
                    )
                }
            },
            text = {
                Text(
                    text = "정말로 탈퇴하시겠습니까?",
                    color = StudifyColors.BLACK
                )
            }
        )
    }
}

@Preview
@Composable
private fun AccountDeletionModalPreview() {
    AccountDeletionModal(
        showModal = true,
        onDismiss = {},
        onConfirm = {}
    )
}