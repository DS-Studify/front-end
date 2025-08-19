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
import com.ds.studify.core.data.model.BarData
import com.ds.studify.core.data.model.Segment
import com.ds.studify.core.data.model.TimeRange
import com.ds.studify.core.domain.entity.TimeEntry
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

@SuppressLint("DefaultLocale")
fun formatTimeInKorean(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return when {
        hours > 0 -> {
            if (minutes > 0) {
                String.format("%d시간 %d분", hours, minutes)
            } else {
                String.format("%d시간", hours)
            }
        }

        minutes > 0 -> {
            if (remainingSeconds > 0) {
                String.format("%d분 %d초", minutes, remainingSeconds)
            } else {
                String.format("%d분", minutes)
            }
        }

        else -> {
            String.format("%d초", remainingSeconds)
        }
    }
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

fun getBarRatioInSeconds(
    timeLogs: Map<Int, List<TimeRange>>,
    start: LocalDateTime,
    end: LocalDateTime
): List<BarData> {
    val totalSeconds = Duration.between(start, end).seconds.toFloat()

    val barList = timeLogs.map { (stateId, logs) ->
        val segments = logs.map { log ->
            val startRatio = Duration.between(start, log.startTime).seconds / totalSeconds
            val endRatio = Duration.between(start, log.endTime).seconds / totalSeconds
            val height = endRatio - startRatio
            Segment(startRatio, height)
        }
        BarData(stateId, segments)
    }

    return barList
}

fun getTimeLog(
    timeLogs: Map<String, List<TimeEntry>>
): Map<Int, List<TimeRange>> {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    val timeLog = timeLogs.mapNotNull { (k, list) ->
        val key = k.toIntOrNull() ?: return@mapNotNull null
        val ranges = list.mapNotNull { timeEntry ->
            runCatching {
                TimeRange(
                    startTime = LocalDateTime.parse(timeEntry.startTime, formatter),
                    endTime = LocalDateTime.parse(timeEntry.endTime, formatter)
                )
            }.getOrNull()
        }
        key to ranges
    }.toMap()

    return timeLog
}