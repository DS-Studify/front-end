package com.ds.studify.feature.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.view.WindowManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ds.studify.core.designsystem.theme.StudifyColors
import com.ds.studify.core.designsystem.theme.Typography
import com.ds.studify.core.ui.extension.formatRecordDuration
import com.ds.studify.feature.camera.component.FlipButton
import com.ds.studify.feature.camera.component.RecordButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun CameraRoute(
    onRecordCloseClick: (Long) -> Unit
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
    onRecordCloseClick: (Long) -> Unit
) {
    val viewModel: CameraViewModel = hiltViewModel()
    val uiState by viewModel.collectAsState()

    viewModel.collectSideEffect {
        when (it) {
            is CameraSideEffect.LoadStudyRecordId -> {
                onRecordCloseClick(it.id)
            }
        }
    }

    when (cameraState.value) {
        is CameraPermissionState.PermissionNotGranted -> {
            RequestPermission(setState)
        }

        is CameraPermissionState.Success -> {
            when (val state = uiState) {
                is CameraUiState.Data -> {
                    CameraScreen(
                        viewModel = viewModel,
                        uiState = state,
                        onEvent = viewModel::onEvent
                    )
                }

                is CameraUiState.Loading -> {
                    LoadingScreen()
                }
            }
        }
    }
}

@Composable
internal fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    uiState: CameraUiState.Data,
    onEvent: (LogEvent) -> Unit
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
    val faceLandmarks = cameraX.getFaceLandmarks().collectAsState(emptyList())

    LaunchedEffect(Unit) {
        cameraX.initialize(context = context)
        previewView.value = cameraX.getPreviewView()
    }

    LaunchedEffect(handLandmarks.value) {
        if (handLandmarks.value.isNotEmpty()) {
            val singleHand = handLandmarks.value.filter { it.handIndex == 0 }
            viewModel.classifyHand(singleHand)
        }
    }

    LaunchedEffect(poseLandmarks.value) {
        viewModel.classifyPose(poseLandmarks.value, faceLandmarks.value)
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

    DisposableEffect(Unit) {
        context.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) // 화면 켜짐 유지

        onDispose {
            context.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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
                style = Typography.titleMedium,
                color = StudifyColors.WHITE
            )
        }

        if (recordingState.value == RecordingState.OnRecord) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 20.dp, start = 30.dp)
            ) {
                if (uiState.poseLabel != null) {
                    Text(
                        modifier = Modifier
                            .padding(top = 10.dp),
                        text = uiState.studyState,
                        style = Typography.titleMedium,
                        color = if (uiState.studyState == "공부 중지") StudifyColors.PK03 else StudifyColors.WHITE
                    )
                }
            }
        }

        if (recordingState.value == RecordingState.Idle) {
            FlipButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .navigationBarsPadding()
                    .padding(top = 32.dp, end = 36.dp),
                onClick = {
                    cameraX.flipCameraFacing()
                }
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .navigationBarsPadding()
                .padding(end = 16.dp)
        ) {
            when (recordingState.value) {
                is RecordingState.Idle -> {
                    RecordButton(
                        recordingState = recordingState.value,
                        onClick = {
                            cameraX.startRecordVideo()
                            onEvent(LogEvent.StartRecording)
                        }
                    )
                }

                is RecordingState.OnRecord -> {
                    RecordButton(
                        recordingState = recordingState.value,
                        onClick = {
                            cameraX.stopRecordVideo()
                            onEvent(LogEvent.SaveLogRequest)
                        }
                    )
                }
            }
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

@Composable
private fun LoadingScreen() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Asset("lottie_loading.json")
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = StudifyColors.WHITE)
    ) {
        LottieAnimation(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp),
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
    }
}

@Preview
@Composable
private fun CameraScreenPreview() {
    CameraScreen(
        uiState = CameraUiState.Data(
            isPenInHand = false,
            poseLabel = null,
            studyState = ""
        ),
        onEvent = {}
    )
}