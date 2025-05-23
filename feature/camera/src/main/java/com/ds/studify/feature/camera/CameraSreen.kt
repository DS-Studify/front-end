package com.ds.studify.feature.camera

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun CameraRoute(

) {
    val uiState =
        MutableStateFlow<CameraPermissionState>(CameraPermissionState.PermissionNotGranted)

    CheckCameraPermission(uiState.collectAsState()) {
        uiState.value = it
    }
}

@Composable
internal fun CheckCameraPermission(
    cameraState: State<CameraPermissionState>,
    setState: (CameraPermissionState) -> Unit
) {
    when (cameraState.value) {
        is CameraPermissionState.PermissionNotGranted -> {
            RequestPermission(setState)
        }

        is CameraPermissionState.Success -> {
            CameraScreen()
        }
    }
}

@Composable
internal fun CameraScreen(

) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraScope = rememberCoroutineScope()
    val context = LocalContext.current as Activity
    val cameraX = remember { CameraXFactory.create() }
    val previewView = remember { mutableStateOf<PreviewView?>(null) }
    val facing = cameraX.getFacingState().collectAsState()
    val recordingState = cameraX.getRecordingState().collectAsState()
    val recordingInfo = cameraX.getRecordingInfo().collectAsState(RecordingInfo(0, 0))

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

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 25.dp)
        ) {
            Text(
                "${(recordingInfo.value.duration / 1000000000.0)} second",
                fontSize = 20.sp,
                color = Color.White
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                when (recordingState.value) {
                    is RecordingState.OnRecord -> {
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.closeRecordVideo()
                            }
                        ) {
                            Text("close")
                        }
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.stopRecordVideo()
                            }
                        ) {
                            Text("stop")
                        }
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.pauseRecordVideo()
                            }
                        ) {
                            Text("pause")
                        }
                    }

                    is RecordingState.Idle -> {
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.startRecordVideo()
                            }
                        ) {
                            Text("record")
                        }
                    }

                    is RecordingState.Paused -> {
                        Button(
                            modifier = Modifier
                                .padding(10.dp),
                            onClick = {
                                cameraX.resumeRecordVideo()
                            }
                        ) {
                            Text("resume")
                        }
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier
                        .padding(10.dp),
                    onClick = {
                        cameraX.flipCameraFacing()
                    }
                ) {
                    Text(if (facing.value == CameraSelector.LENS_FACING_FRONT) "back" else "front")
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

@Preview
@Composable
private fun CameraScreenPreview() {
    CameraScreen()
}