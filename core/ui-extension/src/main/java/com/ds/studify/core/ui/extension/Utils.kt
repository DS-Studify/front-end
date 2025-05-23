package com.ds.studify.core.ui.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

@SuppressLint("DefaultLocale")
fun formatRecordDuration(duration: Long): String {
    val totalSeconds = duration / 1_000_000_000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Modifier.clickableWithoutRippleEffect(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = clickable(
    indication = null,
    interactionSource = null,
    enabled = enabled,
    onClickLabel = onClickLabel,
    role = role,
    onClick = onClick
)