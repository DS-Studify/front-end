package com.ds.studify.feature.camera

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface CameraX {

    fun initialize(context: Context)
    fun startCamera(lifecycleOwner: LifecycleOwner)
    fun startRecordVideo()
    fun stopRecordVideo()
    fun resumeRecordVideo()
    fun pauseRecordVideo()
    fun closeRecordVideo()
    fun flipCameraFacing()
    fun unBindCamera()
    fun getPreviewView(): PreviewView
    fun getFacingState(): StateFlow<Int>
    fun getRecordingState(): StateFlow<RecordingState>
    fun getRecordingInfo(): SharedFlow<RecordingInfo>
}