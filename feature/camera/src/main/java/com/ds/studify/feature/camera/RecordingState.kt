package com.ds.studify.feature.camera

sealed class RecordingState {
    data object Idle : RecordingState()
    data object OnRecord : RecordingState()
    data object Paused : RecordingState()
}