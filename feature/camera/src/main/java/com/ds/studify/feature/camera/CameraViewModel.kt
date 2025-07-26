package com.ds.studify.feature.camera

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ds.studify.core.ui.extension.calculateAngle3D
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONArray
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

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

private var prevState: CameraUiState? = null
private var currentPeriodStart: Instant? = null
val logList:MutableList<String> = mutableListOf()
private var isRecording: Boolean = false
private var logJob: Job? = null

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

    // 펜 쥔 손 분류 결과에 따른 공부 상태
    fun getStudyState(isPenInHand: Boolean): String {
        return if (isPenInHand) "공부 중" else "공부 중지"
    }
    // 자세 분류 결과에 따른 공부 상태
    fun getPoseState(poseLabel: PoseLabel?): String {
        return when (poseLabel) {
            PoseLabel.GOOD_POSE -> "집중상태"

            PoseLabel.NFOCUS_LEAN_BACK,
            PoseLabel.NFOCUS_LEAN_FOWARD,
            PoseLabel.NFOCUS_LEAN_SIDE -> "집중력 저하 상태"

            PoseLabel.SLEEP_HEAD_BACK,
            PoseLabel.SLEEP_HEAD_DOWN -> "수면 상태"

            else -> "알 수 없음"
        }
    }

    fun classifyHand(handLandmarks: List<HandLandmark>) {
        if (handLandmarks.size != 21) return

        val input = prepareHandModelInput(handLandmarks)
        val result = handClassifier.predict(input)  // [0.0 ~ 1.0]

        val isPen = result >= 0.5f  // 0.5 이상 -> 펜 쥔 손

        intent {
            reduce { state.copy(isPenInHand = isPen) }
        }
    }

    fun classifyPose(poseLandmarks: List<PoseLandmark>, faceLandmarks: List<FaceLandmark>) {
        val input = preparePoseModelInput(poseLandmarks, faceLandmarks)
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

    private fun preparePoseModelInput(poseLandmarks: List<PoseLandmark>, faceLandmarks: List<FaceLandmark>): FloatArray {
        if (poseLandmarks.size < 23 || faceLandmarks.isEmpty()) return FloatArray(93)

        val input = mutableListOf<Float>()

        // faceMesh 사용 인덱스
        val faceIndices = listOf(
            1, 4, 10, 13, 14, 33, 78, 133, 145, 152, 159, 199, 234, 263, 308, 362, 374, 386, 454)
        // faceMesh 랜드마크 x, y, z
        for (i in faceIndices) {
            val lm = faceLandmarks.firstOrNull{ it.landmarkIndex == i } ?: continue
            input.add(lm.x)
            input.add(lm.y)
            input.add(lm.z)
        }

        // pose 랜드마크 11 ~ 22의 x, y, z, v
        for (i in 11..22) {
            val lm = poseLandmarks.firstOrNull { it.landmarkIndex == i } ?: continue
            input.add(lm.x)
            input.add(lm.y)
            input.add(lm.z)
        }

        return input.toFloatArray() // pose 36 + faceMesh57 총 93개
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

    // 상태 변화시 로그 저장
    fun saveLog(currentState: CameraUiState) {
        val now = Instant.now()

        val hasChanged = prevState?.let {
            it.isPenInHand != currentState.isPenInHand || getPoseState(it.poseLabel) != getPoseState(
                currentState.poseLabel
            )
        } ?: true // 처음 상태는 무조건 저장

        if (hasChanged) {
            prevState?.let { prev ->
                currentPeriodStart?.let { start ->
                    val studyState = getStudyState(prev.isPenInHand)
                    val poseState = getPoseState(prev.poseLabel)
                    val duration = Duration.between(start, now).seconds

                    val json = JSONObject().apply {
                        put("state_name", studyState)
                        put("pose_name", poseState)
                        put("duration", duration)

                        val periods = JSONArray().apply {
                            put(JSONObject().apply {
                                put("startTime", start.toString())
                                put("endTime", now.toString())
                            })
                        }
                        put("periods", periods)
                    }

                    logList.add(json.toString())

                }

            }

            // 현재 상태 갱신
            currentPeriodStart = now
            prevState = currentState

        }
    }

    fun startRecordingLog() {
        logList.clear()
        prevState = null
        currentPeriodStart = null
        isRecording = true

        logJob?.cancel()

        logJob = viewModelScope.launch {
            while(isRecording) {
                val currentState = container.stateFlow.value
                saveLog(currentState)
                delay(1000L)
            }
        }

    }

    fun stopRecordingLog() {
        val currentState = container.stateFlow.value
        saveLog(currentState)

        isRecording = false
        logJob?.cancel()
        logJob = null

        prevState = null
        currentPeriodStart = null
    }

}