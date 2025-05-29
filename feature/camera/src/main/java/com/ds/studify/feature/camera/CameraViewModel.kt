package com.ds.studify.feature.camera

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ds.studify.core.ui.extension.calculateAngle3D
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class CameraUiState(
    val isPenInHand: Boolean
)

@HiltViewModel
class CameraViewModel @Inject constructor(
    application: Application
) : ViewModel(), ContainerHost<CameraUiState, Nothing> {

    override val container: Container<CameraUiState, Nothing> = container(
        CameraUiState(isPenInHand = false)
    )

    private val handClassifier = HandClassifier(application)

    fun classifyHand(handLandmarks: List<HandLandmark>) {
        if (handLandmarks.size != 21) return

        val input = prepareModelInput(handLandmarks)
        val result = handClassifier.predict(input)  // [0.0 ~ 1.0]

        val isPen = result >= 0.5f  // 0.5 이상 -> 펜 쥔 손

        intent {
            reduce { state.copy(isPenInHand = isPen) }
        }
    }

    private fun prepareModelInput(handLandmarks: List<HandLandmark>): FloatArray {
        val wrist = handLandmarks.firstOrNull { it.landmarkIndex == 0 } ?: return FloatArray(73)

        val relativeCoords = mutableListOf<Float>()

        // 0번 손목 좌표는 (0, 0, 0)으로 고정
        relativeCoords.add(0f)
        relativeCoords.add(0f)
        relativeCoords.add(0f)

        // 1번부터 20번까지 손목 기준 상대 좌표로 변환
        for (i in 1..20) {
            val lm = handLandmarks.firstOrNull { it.landmarkIndex == i } ?: continue
            relativeCoords.add(lm.x - wrist.x)
            relativeCoords.add(lm.y - wrist.y)
            relativeCoords.add(lm.z - wrist.z)
        }

        // 관절 각도 10개 추가
        val angles = calculateJointAngles(handLandmarks)
        Log.d("HandModel-좌표", "$relativeCoords $angles")

        return (relativeCoords + angles).toFloatArray()
    }

    private fun calculateJointAngles(landmarks: List<HandLandmark>): List<Float> {
        fun get(index: Int): FloatArray {
            val l = landmarks.first { it.landmarkIndex == index }
            return floatArrayOf(l.x, l.y, l.z)
        }

        return listOf(
            // 엄지 (1-2-3, 2-3-4)
            calculateAngle3D(get(1), get(2), get(3)),
            calculateAngle3D(get(2), get(3), get(4)),

            // 검지 (5-6-7, 6-7-8)
            calculateAngle3D(get(5), get(6), get(7)),
            calculateAngle3D(get(6), get(7), get(8)),

            // 중지 (9-10-11, 10-11-12)
            calculateAngle3D(get(9), get(10), get(11)),
            calculateAngle3D(get(10), get(11), get(12)),

            // 약지 (13-14-15, 14-15-16)
            calculateAngle3D(get(13), get(14), get(15)),
            calculateAngle3D(get(14), get(15), get(16)),

            // 새끼손가락 (17-18-19, 18-19-20)
            calculateAngle3D(get(17), get(18), get(19)),
            calculateAngle3D(get(18), get(19), get(20))
        )
    }
}