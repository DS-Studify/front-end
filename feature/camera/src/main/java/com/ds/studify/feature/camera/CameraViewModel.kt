package com.ds.studify.feature.camera

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ds.studify.core.data.repository.StudyRepository
import com.ds.studify.core.domain.entity.CameraEntity
import com.ds.studify.core.domain.entity.TimeLog
import com.ds.studify.core.ui.extension.calculateAngle3D
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class CameraUiState(
    val isPenInHand: Boolean,
    val poseLabel: PoseLabel?,
    val studyState: String = ""
)

sealed interface LogEvent {
    data object StartRecording : LogEvent
    data object SaveLogRequest : LogEvent
}

data class StableState(
    var stablePenInHand: Boolean = false,
    var stablePoseLabel: PoseLabel? = null
)

enum class PoseLabel(
    val label: String
) {
    GOOD_POSE("집중 자세"),
    NFOCUS_LEAN_FOWARD("집중도 저하 자세"),
    NFOCUS_LEAN_BACK("집중도 저하 자세"),
    NFOCUS_LEAN_SIDE("집중도 저하 자세"),
    SLEEP_HEAD_DOWN("수면 자세"),
    SLEEP_HEAD_BACK("수면 자세"),
    AWAY("자리 비움")
}

data class LogRecordingState(
    var prevState: CameraUiState? = null,
    var isRecording: Boolean = false,
    var overallStart: LocalDateTime? = null,
    var overallEnd: LocalDateTime? = null,
    val logMap: MutableMap<Int, MutableList<TimeLog>> = mutableMapOf(),
    val stateStartTimeMap: MutableMap<Int, LocalDateTime> = mutableMapOf()
)

sealed interface CameraSideEffect {
    data class LoadStudyRecordId(val id: Long) : CameraSideEffect
}

@HiltViewModel
class CameraViewModel @Inject constructor(
    application: Application,
    private val studyRepository: StudyRepository
) : ViewModel(), ContainerHost<CameraUiState, CameraSideEffect> {

    override val container: Container<CameraUiState, CameraSideEffect> = container(
        CameraUiState(
            isPenInHand = false,
            poseLabel = null,
            studyState = ""
        )
    )

    fun saveLogToServer() = intent {
        if (recordingState.overallStart == null) return@intent

        val entity = recordingState.toCameraEntity()

        studyRepository.postRecord(entity)
            .onSuccess { id ->
                recordingState.logMap.clear()
                recordingState.stateStartTimeMap.clear()
                postSideEffect(CameraSideEffect.LoadStudyRecordId(id))
            }
            .onFailure {
            }

    }

    fun onEvent(event: LogEvent) {
        when (event) {
            is LogEvent.StartRecording -> intent {
                if (recordingState.isRecording) return@intent
                startRecordingLog()
            }

            is LogEvent.SaveLogRequest -> intent {
                stopRecordingLog()
                saveLogToServer()
            }
        }
    }

    fun LogRecordingState.toCameraEntity(): CameraEntity {
        val date = overallStart?.toLocalDate().toString()
        val startTime = overallStart?.format(timeFormatter).toString()
        val endTime = overallEnd?.format(timeFormatter).toString()

        val timeLogMap: Map<String, List<TimeLog>> =
            logMap.mapKeys { (k, _) -> k.toString() }

        return CameraEntity(
            date = date,
            startTime = startTime,
            endTime = endTime,
            timeLog = timeLogMap
        )
    }

    private val handClassifier = HandClassifier(application)
    private val poseClassifier = PoseClassifier(application)
    private val recordingState = LogRecordingState()

    private val stableState = StableState()
    private var penCount = 0
    private var poseCount = 0
    private var requiredCount = 3
    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    var sleepCount = 0
    var prevPose: String? = null
    var stablestudyState: String = ""

    // 자세 분류 결과에 따른 공부 상태
    fun getPoseState(poseLabel: PoseLabel?): Int {
        return when (poseLabel) {
            PoseLabel.GOOD_POSE -> 1
            PoseLabel.NFOCUS_LEAN_BACK,
            PoseLabel.NFOCUS_LEAN_FOWARD,
            PoseLabel.NFOCUS_LEAN_SIDE -> 2

            PoseLabel.SLEEP_HEAD_BACK,
            PoseLabel.SLEEP_HEAD_DOWN -> 3

            PoseLabel.AWAY -> 4

            else -> -1
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
        if (stableState.stablePoseLabel == null) {
            stableState.stablePoseLabel = currentValue
            return
        }
        if (currentValue == PoseLabel.AWAY) {
            stableState.stablePoseLabel = PoseLabel.AWAY
            poseCount = 0
            return
        }
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

    // studyState 업데이트
    fun getStudyState(isPenInHand: Boolean, poseLabel: PoseLabel?): String {
        return when (poseLabel?.label) {
            "집중 자세" -> {
                sleepCount = 0; prevPose = null
                stablestudyState = if (isPenInHand) "집중 상태" else "공부 중지"
                stablestudyState
            }

            "집중도 저하 자세" -> {
                sleepCount = 0; prevPose = null
                stablestudyState = if (isPenInHand) "집중도 저하 상태" else "공부 중지"
                stablestudyState
            }

            "수면 자세" -> {
                prevPose = "수면 자세"
                sleepCount++
                Log.d("sleepCount", sleepCount.toString())
                if (sleepCount >= 6) {
                    stablestudyState = "수면 상태"
                }
                stablestudyState
            }

            "자리 비움" -> {
                sleepCount = 0; prevPose = null
                stablestudyState = ""
                stablestudyState
            }

            else -> ""
        }
    }

    private fun updateUiState(
        isPenInHand: Boolean? = null,
        poseLabel: PoseLabel? = null,
    ) = intent {
        val newIsPen = isPenInHand ?: state.isPenInHand
        val newPose = poseLabel ?: state.poseLabel

        val newStudyState = getStudyState(newIsPen, newPose)

        reduce {
            state.copy(
                isPenInHand = newIsPen,
                poseLabel = newPose,
                studyState = newStudyState
            )
        }
    }

    fun classifyHand(handLandmarks: List<HandLandmark>) {
        if (handLandmarks.size != 21) return

        val input = prepareHandModelInput(handLandmarks)
        val result = handClassifier.predict(input)  // [0.0 ~ 1.0]

        val isPen = result >= 0.5f  // 0.5 이상 -> 펜 쥔 손

        updateUiState(isPenInHand = isPen)
        updatePenState(isPen)
        saveLog()
    }

    fun classifyPose(poseLandmarks: List<PoseLandmark>, faceLandmarks: List<FaceLandmark>) {
        // 시작 후 랜드 마크 입력 대기
        if (stableState.stablePoseLabel == null) {
            if (poseLandmarks.isEmpty() || faceLandmarks.isEmpty()) return
        }

        val input = preparePoseModelInput(poseLandmarks, faceLandmarks)
        val result = poseClassifier.predict(input)

        val predictedLabel = result.indices.maxByOrNull { result[it] } ?: -1
        val labels = listOf(
            PoseLabel.GOOD_POSE, PoseLabel.NFOCUS_LEAN_BACK,
            PoseLabel.NFOCUS_LEAN_FOWARD, PoseLabel.NFOCUS_LEAN_SIDE,
            PoseLabel.SLEEP_HEAD_BACK, PoseLabel.SLEEP_HEAD_DOWN,
        )
        updateUiState(poseLabel = labels[predictedLabel])
        updatePoseState(labels[predictedLabel])
        saveLog()

        if (poseLandmarks.isEmpty() && faceLandmarks.isEmpty()) {
            updateUiState(poseLabel = PoseLabel.AWAY)
            updatePoseState(PoseLabel.AWAY)
            saveLog()
        }
        Log.d("poseState", stableState.stablePoseLabel.toString())
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
        val poseState = getPoseState(stableState.stablePoseLabel)
        val hasPen = stableState.stablePenInHand

        when (poseState) {
            1 -> {
                if (hasPen) {
                    result.add(1)
                    result.add(2)
                } else {
                    result.add(6)
                }
            }

            2 -> {
                if (hasPen) {
                    result.add(1)
                    result.add(3)
                } else {
                    result.add(6)
                }
            }

            3 -> {
                result.add(4)
            }

            4 -> {
                result.add(5)
            }
        }
        return result

    }

    // 상태 변화 감지 로그 저장
    fun saveLog() {
        if (!recordingState.isRecording) return

        val currentStates = checkStates()
        Log.d("poseState2", currentStates.toString())
        val prevStates = recordingState.stateStartTimeMap.keys.toList()

        // 종료된 상태
        prevStates.forEach { stateId ->
            if (!currentStates.contains(stateId)) {
                val startTime = recordingState.stateStartTimeMap[stateId] ?: return@forEach
                val list = recordingState.logMap.getOrPut(stateId) { mutableListOf() }
                list.add(
                    TimeLog(
                        startTime = startTime.format(timeFormatter),
                        endTime = LocalDateTime.now().format(timeFormatter)
                    )
                )
                recordingState.stateStartTimeMap.remove(stateId)
            }
        }
        // 새로 시작된 상태
        currentStates.forEach { stateId ->
            if (!recordingState.stateStartTimeMap.containsKey(stateId)) {
                val start = if (recordingState.logMap.values.all { it.isEmpty() }) {
                    recordingState.overallStart ?: LocalDateTime.now()
                } else {
                    LocalDateTime.now()
                }
                recordingState.stateStartTimeMap[stateId] = start
            }
        }
    }

    // 로그 기록 시작
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

        checkStates().forEach { stateId ->
            recordingState.stateStartTimeMap[stateId] = LocalDateTime.now()
        }
    }

    // 로그 기록 종료
    fun stopRecordingLog() {
        if (!recordingState.isRecording) return
        recordingState.overallEnd = LocalDateTime.now()

        // 종료되지 않은 상태 처리
        recordingState.stateStartTimeMap.forEach { (stateId, startTime) ->
            val list = recordingState.logMap.getOrPut(stateId) { mutableListOf() }
            list.add(
                TimeLog(
                    startTime = startTime.format(timeFormatter),
                    endTime = recordingState.overallEnd!!.format(timeFormatter)
                )
            )
        }

        recordingState.isRecording = false
    }
}