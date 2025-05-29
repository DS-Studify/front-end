package com.ds.studify.core.ui.extension

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlin.math.acos
import kotlin.math.sqrt

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModelOnGraph(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

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

fun calculateAngle3D(a: FloatArray, b: FloatArray, c: FloatArray): Float {
    val ab = floatArrayOf(a[0] - b[0], a[1] - b[1], a[2] - b[2])
    val cb = floatArrayOf(c[0] - b[0], c[1] - b[1], c[2] - b[2])

    val dot = ab[0] * cb[0] + ab[1] * cb[1] + ab[2] * cb[2]
    val abNorm = sqrt((ab[0] * ab[0] + ab[1] * ab[1] + ab[2] * ab[2]).toDouble())
    val cbNorm = sqrt((cb[0] * cb[0] + cb[1] * cb[1] + cb[2] * cb[2]).toDouble())

    val cosine = (dot / (abNorm * cbNorm + 1e-6)).coerceIn(-1.0, 1.0)
    return Math.toDegrees(acos(cosine)).toFloat()
}