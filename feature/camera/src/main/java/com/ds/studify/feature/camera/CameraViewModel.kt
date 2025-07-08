package com.ds.studify.feature.camera

import android.app.Application
import androidx.lifecycle.ViewModel
import com.ds.studify.core.ui.extension.calculateAngle3D
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject
import kotlin.math.acos
import kotlin.math.sqrt

data class CameraUiState(
    val isPenInHand: Boolean,
    val poseLabel: PoseLabel?
)

enum class PoseLabel(
    val label: String
) {
    GOOD_POSE("바른 자세"),
    NFOCUS_LEAN_FOWARD("한쪽으로 엎드린 자세 (집중도 저하)"),
    NFOCUS_LEAN_BACK("상반신이 뒤로 기운 자세 (집중도 저하)"),
    NFOCUS_LEAN_SIDE("옆으로 휜 자세 (집중도 저하)"),
    SLEEP_HEAD_DOWN("머리를 숙여 엎드린 자세 (수면)"),
    SLEEP_HEAD_BACK("머리가 뒤로 넘어간 자세 (수면)")
}

@HiltViewModel
class CameraViewModel @Inject constructor(
    application: Application
) : ViewModel(), ContainerHost<CameraUiState, Nothing> {

    override val container: Container<CameraUiState, Nothing> = container(
        CameraUiState(
            isPenInHand = false,
            poseLabel = null
        )
    )

    private val handClassifier = HandClassifier(application)
    private val poseClassifier = PoseClassifier(application)

    fun classifyHand(handLandmarks: List<HandLandmark>) {
        if (handLandmarks.size != 21) return

        val input = prepareHandModelInput(handLandmarks)
        val result = handClassifier.predict(input)  // [0.0 ~ 1.0]

        val isPen = result >= 0.5f  // 0.5 이상 -> 펜 쥔 손

        intent {
            reduce { state.copy(isPenInHand = isPen) }
        }
    }

    fun classifyPose(poseLandmarks: List<PoseLandmark>) {
        val input = preparePoseModelInput(poseLandmarks)
        val result = poseClassifier.predict(input)

        val predictedLabel = result.indices.maxByOrNull { result[it] } ?: -1
        val labels = listOf(
            PoseLabel.GOOD_POSE, PoseLabel.NFOCUS_LEAN_BACK,
            PoseLabel.NFOCUS_LEAN_FOWARD, PoseLabel.NFOCUS_LEAN_SIDE,
            PoseLabel.SLEEP_HEAD_BACK, PoseLabel.SLEEP_HEAD_DOWN,
        )
        intent {
            reduce { state.copy(poseLabel = labels[predictedLabel]) }
        }
    }

    private fun prepareHandModelInput(handLandmarks: List<HandLandmark>): FloatArray {
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

        return (relativeCoords + angles).toFloatArray()
    }

    private fun preparePoseModelInput(poseLandmarks: List<PoseLandmark>): FloatArray {
        if (poseLandmarks.size < 23) return FloatArray(94)

        val input = mutableListOf<Float>()

        // 랜드마크 0 ~ 22의 x, y, z, v
        for (i in 0..22) {
            val lm = poseLandmarks.firstOrNull { it.landmarkIndex == i } ?: continue
            input.add(lm.x)
            input.add(lm.y)
            input.add(lm.z)
            input.add(lm.visibility)
        }

        // angle 추가
        val angle1 = calculateAngleBetweenLandmarks(poseLandmarks, 11, 0, 12)  // 왼어깨-머리-오른어깨
        val angle2 = calculateAngleBetweenLandmarks(poseLandmarks, 12, 0, 11)  // 오른어깨-머리-왼어깨

        input.add(angle1)
        input.add(angle2)

        return input.toFloatArray() // 총 92 + 2(각도) = 94개
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

    private fun calculateAngleBetweenLandmarks(
        landmarks: List<PoseLandmark>,
        aIndex: Int, bIndex: Int, cIndex: Int
    ): Float {
        val a = landmarks.first { it.landmarkIndex == aIndex }
        val b = landmarks.first { it.landmarkIndex == bIndex }
        val c = landmarks.first { it.landmarkIndex == cIndex }

        val ab = floatArrayOf(a.x - b.x, a.y - b.y, a.z - b.z)
        val cb = floatArrayOf(c.x - b.x, c.y - b.y, c.z - b.z)

        val dot = ab[0]*cb[0] + ab[1]*cb[1] + ab[2]*cb[2]
        val abNorm = sqrt((ab[0]*ab[0] + ab[1]*ab[1] + ab[2]*ab[2]).toDouble())
        val cbNorm = sqrt((cb[0]*cb[0] + cb[1]*cb[1] + cb[2]*cb[2]).toDouble())

        val cosine = (dot / (abNorm * cbNorm + 1e-6)).coerceIn(-1.0, 1.0)
        return Math.toDegrees(acos(cosine)).toFloat()
    }
}