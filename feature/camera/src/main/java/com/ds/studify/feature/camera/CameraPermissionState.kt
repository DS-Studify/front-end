package com.ds.studify.feature.camera

sealed class CameraPermissionState {
    data object PermissionNotGranted: CameraPermissionState()
    data object Success: CameraPermissionState()
}