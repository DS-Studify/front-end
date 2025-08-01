package com.ds.studify.feature.camera

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ds.studify.core.ui.extension.calculateAngle3D
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONArray
import org.json.JSONObject
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CameraUiState(
    val isPenInHand: Boolean,
    val poseLabel: PoseLabel?
)

data class StableState(
    var stablePenInHand: Boolean = false,
    var stablePoseLabel: PoseLabel? = null
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

data class LogRecordingState(
    var prevState: CameraUiState? = null,
    var isRecording: Boolean = false,
    var overallStart: LocalDateTime? = null,
    var overallEnd: LocalDateTime? = null,
    val logMap: MutableMap<Int, MutableList<MutableMap<String, String>>> = mutableMapOf(),
    val stateStartTimeMap: MutableMap<Int, LocalDateTime> = mutableMapOf()
)

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
    private val recordingState = LogRecordingState()

    private val stableState = StableState()
    private var penCount = 0
    private var poseCount = 0
    private var requiredCount = 3
    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    // 자세 분류 결과에 따른 공부 상태
    fun getPoseState(poseLabel: PoseLabel?): Int {
        return when (poseLabel) {
            PoseLabel.GOOD_POSE -> 1

            PoseLabel.NFOCUS_LEAN_BACK,
            PoseLabel.NFOCUS_LEAN_FOWARD,
            PoseLabel.NFOCUS_LEAN_SIDE -> 2

            PoseLabel.SLEEP_HEAD_BACK,
            PoseLabel.SLEEP_HEAD_DOWN -> 3

            else -> 0
        }
    }

    // 펜 상태 연속 3번 지연 업데이트
    fun updatePenState(currentValue: Boolean) {
        if (currentValue == stableState.stablePenInHand) {
            penCount = 0
            return
        }
        if (penCount == 0 || currentValue != container.stateFlow.value.isPenInHand) {
            penCount = 1
        } else {
            penCount++
        }
        if (penCount >= requiredCount) {
            stableState.stablePenInHand = currentValue
            penCount = 0
        }
    }

    // 자세 상태 연속 3번 지연 업데이트
    fun updatePoseState(currentValue: PoseLabel?) {
        if (currentValue == stableState.stablePoseLabel) {
            poseCount = 0
            return
        }
        if (poseCount == 0 || currentValue != container.stateFlow.value.poseLabel) {
            poseCount = 1
        } else {
            poseCount++
        }
        if (poseCount >= requiredCount) {
            stableState.stablePoseLabel = currentValue
            poseCount = 0
        }
    }

    fun classifyHand(handLandmarks: List<HandLandmark>) {
        if (handLandmarks.size != 21) return

        val input = prepareHandModelInput(handLandmarks)
        val result = handClassifier.predict(input)  // [0.0 ~ 1.0]

        val isPen = result >= 0.5f  // 0.5 이상 -> 펜 쥔 손

        intent {
            reduce { state.copy(isPenInHand = isPen) }
            updatePenState(isPen)
            saveLog()
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
            updatePoseState(labels[predictedLabel])
            saveLog()
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

    private fun preparePoseModelInput(
        poseLandmarks: List<PoseLandmark>,
        faceLandmarks: List<FaceLandmark>
    ): FloatArray {
        if (poseLandmarks.size < 23 || faceLandmarks.isEmpty()) return FloatArray(93)

        val input = mutableListOf<Float>()

        // faceMesh 사용 인덱스
        val faceIndices = listOf(
            1, 4, 10, 13, 14, 33, 78, 133, 145, 152, 159, 199, 234, 263, 308, 362, 374, 386, 454
        )
        // faceMesh 랜드마크 x, y, z
        for (i in faceIndices) {
            val lm = faceLandmarks.firstOrNull { it.landmarkIndex == i } ?: continue
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

    // 조건 확인
    fun checkStates(): List<Int> {
        val result = mutableListOf<Int>()

        if (stableState.stablePenInHand) result.add(1)
        if (stableState.stablePenInHand && getPoseState(stableState.stablePoseLabel) == 1) result.add(
            2
        )
        if (stableState.stablePenInHand && getPoseState(stableState.stablePoseLabel) == 2) result.add(
            3
        )
        if (getPoseState(stableState.stablePoseLabel) == 3) result.add(4)
        if (getPoseState(stableState.stablePoseLabel) == 0) result.add(5)
        if (!stableState.stablePenInHand && (getPoseState(stableState.stablePoseLabel) == 1 || getPoseState(
                stableState.stablePoseLabel
            ) == 2)
        ) result.add(6)

        return result

    }

    // 상태 변화 감지 로그 저장
    fun saveLog() {
        if (!recordingState.isRecording) return

        val currentStates = checkStates()
        val prevStates = recordingState.stateStartTimeMap.keys.toList()

        // 종료된 상태
        prevStates.forEach { stateId ->
            if (!currentStates.contains(stateId)) {
                val startTime = recordingState.stateStartTimeMap[stateId]
                if (startTime != null) {
                    recordingState.logMap[stateId]?.add(
                        mutableMapOf(
                            "startTime" to startTime.format(timeFormatter),
                            "endTime" to LocalDateTime.now().format(timeFormatter)
                        )
                    )
                    recordingState.stateStartTimeMap.remove(stateId)
                }
            }
        }

        // 새로 시작된 상태
        currentStates.forEach { stateId ->
            if (!recordingState.stateStartTimeMap.containsKey(stateId)) {
                recordingState.stateStartTimeMap[stateId] = LocalDateTime.now()
            }
        }
    }

    // 녹화 시작
    fun startRecordingLog() {
        recordingState.isRecording = true
        recordingState.overallStart = LocalDateTime.now()
        recordingState.prevState = CameraUiState(
            stableState.stablePenInHand,
            stableState.stablePoseLabel
        )

        for (i in 1..6) {
            recordingState.logMap[i] = mutableListOf()
        }

        val activateStates = checkStates()
        activateStates.forEach { stateId ->
            recordingState.stateStartTimeMap[stateId] = LocalDateTime.now()
        }
    }

    // 녹화 종료
    fun stopRecordingLog() {
        if (!recordingState.isRecording) return
        recordingState.overallEnd = LocalDateTime.now()

        // 종료되지 않은 상태 처리
        recordingState.stateStartTimeMap.forEach { (stateId, startTime) ->
            recordingState.logMap[stateId]?.add(
                mutableMapOf(
                    "startTime" to startTime.format(timeFormatter),
                    "endTime" to recordingState.overallEnd!!.format(timeFormatter)
                )
            )
        }
        recordingState.stateStartTimeMap.clear()

        //JSON 변환
        val json = JSONObject().apply {
            put("date", recordingState.overallStart?.toLocalDate().toString())
            put("startTime", recordingState.overallStart?.format(timeFormatter))
            put("endTime", recordingState.overallEnd?.format(timeFormatter))

            val timeLogObj = JSONObject()
            for (i in 1..6) {
                val arr = JSONArray()
                recordingState.logMap[i]?.forEach { log ->
                    arr.put(JSONObject(log.toMap()))
                }
                timeLogObj.put(i.toString(), arr)
            }
            put("timeLog", timeLogObj)
        }

        Log.d("stateLog", json.toString())

        recordingState.isRecording = false
    }
}