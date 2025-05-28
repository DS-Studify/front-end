package com.ds.studify.feature.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.ui.extension.formatRecordDuration
import com.ds.studify.feature.camera.component.FlipButton
import com.ds.studify.feature.camera.component.RecordButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun CameraRoute(
    onRecordCloseClick: () -> Unit
) {
    val uiState =
        MutableStateFlow<CameraPermissionState>(CameraPermissionState.PermissionNotGranted)

    CheckCameraPermission(
        cameraState = uiState.collectAsState(),
        onRecordCloseClick = onRecordCloseClick,
        setState = { uiState.value = it }
    )
}

@Composable
internal fun CheckCameraPermission(
    cameraState: State<CameraPermissionState>,
    setState: (CameraPermissionState) -> Unit,
    onRecordCloseClick: () -> Unit
) {
    when (cameraState.value) {
        is CameraPermissionState.PermissionNotGranted -> {
            RequestPermission(setState)
        }

        is CameraPermissionState.Success -> {
            CameraScreen(onRecordCloseClick = onRecordCloseClick)
        }
    }
}

@Composable
internal fun CameraScreen(
    onRecordCloseClick: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current as Activity
    val cameraX = remember { CameraXFactory.create() }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val facing = cameraX.getFacingState().collectAsState()
    val recordingState = cameraX.getRecordingState().collectAsState()
    val recordingInfo = cameraX.getRecordingInfo().collectAsState(RecordingInfo(0, 0))
    val handLandmarks = cameraX.getHandLandmarks().collectAsState(emptyList())
    val poseLandmarks = cameraX.getPoseLandmarks().collectAsState(emptyList())

    LaunchedEffect(Unit) {
        cameraX.initialize(context = context)
        previewView.value = cameraX.getPreviewView()
    }

    DisposableEffect(facing.value) {
        cameraScope.launch(Dispatchers.Main) {
            cameraX.startCamera(lifecycleOwner = lifecycleOwner)
        }
        onDispose {
            cameraX.unBindCamera()
        }
    }

    DisposableEffect(context) {
        context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        onDispose {
            context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        previewView.value?.let { preview ->
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { preview }) {}
        }

        // 손 랜드마크 오버레이 (임시)
        HandLandmarkOverlay(
            landmarks = handLandmarks.value,
            modifier = Modifier.fillMaxSize()
        )

        PoseLandmarkOverlay(
            landmarks = poseLandmarks.value,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 25.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(StudifyColors.PK03),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .padding(horizontal = 9.dp),
                text = formatRecordDuration(recordingInfo.value.duration),
                fontSize = 20.sp,
                color = StudifyColors.WHITE
            )
        }

        if (recordingState.value == RecordingState.Idle) {
            FlipButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 32.dp, end = 38.dp),
                onClick = {
                    cameraX.flipCameraFacing()
                }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 22.dp)
        ) {
            when (recordingState.value) {
                is RecordingState.Idle -> {
                    RecordButton(
                        recordingState = recordingState.value,
                        onClick = {
                            cameraX.startRecordVideo()
                        }
                    )
                }

                is RecordingState.OnRecord -> {
                    RecordButton(
                        recordingState = recordingState.value,
                        onClick = {
                            cameraX.stopRecordVideo()
                            onRecordCloseClick()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun HandLandmarkOverlay(
    landmarks: List<HandLandmark>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {

        landmarks.forEach { landmark ->
            val x = landmark.x * size.width
            val y = landmark.y * size.height
            drawCircle(
                color = if (landmark.handIndex == 0) Color.Red else Color.Blue,
                radius = 6f,
                center = Offset(x, y)
            )
        }

        drawHandConnections(landmarks, size)
    }
}

@Composable
fun PoseLandmarkOverlay(
    landmarks: List<PoseLandmark>,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {

        landmarks.forEach { landmark ->
            val x = landmark.x * size.width
            val y = landmark.y * size.height
            drawCircle(
                color = Color.Yellow,
                radius = 6f,
                center = Offset(x, y)
            )
        }

        drawPoseConnections(landmarks, size)
    }
}

private fun DrawScope.drawHandConnections(landmarks: List<HandLandmark>, size: Size) {

    val connections = listOf(
        // 엄지
        listOf(0, 1, 2, 3, 4),
        // 검지
        listOf(0, 5, 6, 7, 8),
        // 중지
        listOf(0, 9, 10, 11, 12),
        // 약지
        listOf(0, 13, 14, 15, 16),
        // 새끼
        listOf(0, 17, 18, 19, 20),
        // 손바닥
        listOf(0, 5, 9, 13, 17, 0)
    )

    landmarks.groupBy { it.handIndex }.forEach { (handIndex, handLandmarks) ->
        val landmarkMap = handLandmarks.associateBy { it.landmarkIndex }
        val color = if (handIndex == 0) Color.Red else Color.Blue

        connections.forEach { connection ->
            for (i in 0 until connection.size - 1) {
                val start = landmarkMap[connection[i]]
                val end = landmarkMap[connection[i + 1]]

                if (start != null && end != null) {
                    drawLine(
                        color = color,
                        start = Offset(start.x * size.width, start.y * size.height),
                        end = Offset(end.x * size.width, end.y * size.height),
                        strokeWidth = 4f
                    )
                }
            }
        }
    }
}

private fun DrawScope.drawPoseConnections(landmarks: List<PoseLandmark>, size: Size) {
    val landmarkMap = landmarks.associateBy { it.landmarkIndex }

    val connections = listOf(
        // 상체
        11 to 13, 13 to 15,        // 왼쪽 팔
        12 to 14, 14 to 16,        // 오른쪽 팔
        11 to 12,                  // 어깨
        11 to 23, 12 to 24,        // 어깨 ↔ 골반

        // 하체
        23 to 25, 25 to 27,        // 왼쪽 다리
        24 to 26, 26 to 28,        // 오른쪽 다리

        // 척추 & 몸통
        23 to 24,                  // 골반 좌우
        24 to 12, 23 to 11,        // 골반 → 어깨
        27 to 31, 28 to 32         // 발끝
    )

    connections.forEach { (startIdx, endIdx) ->
        val start = landmarkMap[startIdx]
        val end = landmarkMap[endIdx]

        if (start != null && end != null) {
            drawLine(
                color = Color.Green,
                start = Offset(start.x * size.width, start.y * size.height),
                end = Offset(end.x * size.width, end.y * size.height),
                strokeWidth = 4f
            )
        }
    }
}

@Composable
private fun RequestPermission(
    setState: (CameraPermissionState) -> Unit
) {
    val context = LocalContext.current
    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                setState(CameraPermissionState.Success)
            }
        }
    if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
        LaunchedEffect(Unit) {
            cameraLauncher.launch(Manifest.permission.CAMERA)
        }
    } else {
        setState(CameraPermissionState.Success)
    }
}

@Preview
@Composable
private fun CameraScreenPreview() {
    CameraScreen(
        onRecordCloseClick = {}
    )
}