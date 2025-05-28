package com.ds.studify.feature.camera

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

data class HandLandmark(
    val handIndex: Int,
    val landmarkIndex: Int,
    val x: Float,
    val y: Float,
    val z: Float
)

data class PoseLandmark(
    val landmarkIndex: Int,
    val x: Float,
    val y: Float,
    val z: Float,
    val visibility: Float
)

interface CameraX {

    fun initialize(context: Context)
    fun getHandLandmarks(): SharedFlow<List<HandLandmark>>
    fun getPoseLandmarks(): SharedFlow<List<PoseLandmark>>
    fun startCamera(lifecycleOwner: LifecycleOwner)
    fun startRecordVideo()
    fun stopRecordVideo()
    fun flipCameraFacing()
    fun unBindCamera()
    fun getPreviewView(): PreviewView
    fun getFacingState(): StateFlow<Int>
    fun getRecordingState(): StateFlow<RecordingState>
    fun getRecordingInfo(): SharedFlow<RecordingInfo>
}