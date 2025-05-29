package com.ds.studify.feature.camera

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.core.Delegate
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarker
import com.google.mediapipe.tasks.vision.handlandmarker.HandLandmarkerResult
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class CameraXImpl : CameraX {

    companion object {
        private const val MP_HAND_LANDMARKER_TASK = "hand_landmarker.task"
        private const val MP_POSE_LANDMARKER_TASK = "pose_landmarker_lite.task"
        const val DEFAULT_NUM_HANDS = 2
        const val DEFAULT_HAND_DETECTION_CONFIDENCE = 0.3F
        const val DEFAULT_HAND_TRACKING_CONFIDENCE = 0.3F
        const val DEFAULT_HAND_PRESENCE_CONFIDENCE = 0.3F
        const val DEFAULT_NUM_POSES = 1
        const val DEFAULT_POSE_DETECTION_CONFIDENCE = 0.5F
        const val DEFAULT_POSE_TRACKING_CONFIDENCE = 0.5F
        const val DEFAULT_POSE_PRESENCE_CONFIDENCE = 0.5F
    }

    private val _facing = MutableStateFlow(CameraSelector.LENS_FACING_FRONT)
    private val _recordingState = MutableStateFlow<RecordingState>(RecordingState.Idle)
    private val _recordingInfo = MutableSharedFlow<RecordingInfo>()

    private lateinit var previewView: PreviewView
    private lateinit var preview: Preview
    private lateinit var cameraProvider: ListenableFuture<ProcessCameraProvider>
    private lateinit var provider: ProcessCameraProvider
    private lateinit var camera: Camera
    private lateinit var context: Context
    private lateinit var executor: ExecutorService
    private lateinit var recording: Recording
    private lateinit var mediaStoreOutput: MediaStoreOutputOptions
    private lateinit var videoCapture: VideoCapture<Recorder>

    private lateinit var imageAnalyzer: ImageAnalysis

    private var lastAnalysisTime = 0L
    private val analysisInterval = 1000L // 1초 간격

    private var handLandmarker: HandLandmarker? = null
    private var isHandDetectionEnabled = false
    private val _handLandmarks = MutableSharedFlow<List<HandLandmark>>()
    override fun getHandLandmarks(): SharedFlow<List<HandLandmark>> = _handLandmarks.asSharedFlow()

    private var poseLandmarker: PoseLandmarker? = null
    private var isPoseDetectionEnabled = true
    private val _poseLandmarks = MutableSharedFlow<List<PoseLandmark>>()
    override fun getPoseLandmarks(): SharedFlow<List<PoseLandmark>> = _poseLandmarks.asSharedFlow()

    private fun initializeMediaPipe() {
        initializeHands()
        initializePose()
    }

    // 손 탐지 초기화
    private fun initializeHands() {
        try {
            val baseOptions = BaseOptions.builder()
                .setDelegate(Delegate.CPU)
                .setModelAssetPath(MP_HAND_LANDMARKER_TASK)
                .build()

            val options = HandLandmarker.HandLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(RunningMode.VIDEO)
                .setNumHands(DEFAULT_NUM_HANDS)
                .setMinHandDetectionConfidence(DEFAULT_HAND_DETECTION_CONFIDENCE)
                .setMinHandPresenceConfidence(DEFAULT_HAND_TRACKING_CONFIDENCE)
                .setMinTrackingConfidence(DEFAULT_HAND_PRESENCE_CONFIDENCE)
                .build()

            handLandmarker = HandLandmarker.createFromOptions(context, options)
            enableHandDetection()
        } catch (e: Exception) {
            Log.e("MediaPipe", "Failed to initialize Hand landmarker", e)
            disableHandDetection()
        }
    }

    // 포즈 탐지 초기화
    private fun initializePose() {
        try {
            val baseOptions = BaseOptions.builder()
                .setDelegate(Delegate.CPU)
                .setModelAssetPath(MP_POSE_LANDMARKER_TASK)
                .build()

            val options = PoseLandmarker.PoseLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setRunningMode(RunningMode.VIDEO)
                .setNumPoses(DEFAULT_NUM_POSES)
                .setMinPoseDetectionConfidence(DEFAULT_POSE_DETECTION_CONFIDENCE)
                .setMinPosePresenceConfidence(DEFAULT_POSE_TRACKING_CONFIDENCE)
                .setMinTrackingConfidence(DEFAULT_POSE_PRESENCE_CONFIDENCE)
                .build()

            poseLandmarker = PoseLandmarker.createFromOptions(context, options)
            enablePoseDetection()
        } catch (e: Exception) {
            Log.e("MediaPipe", "Failed to initialize Pose landmarker", e)
            disablePoseDetection()
        }
    }

    // 손 검출 결과 처리
    private fun processHandsResult(result: HandLandmarkerResult) {
        val landmarks = mutableListOf<HandLandmark>()

        result.landmarks().forEachIndexed { handIndex, handLandmarks ->
            handLandmarks.forEachIndexed { landmarkIndex, landmark ->
                landmarks.add(
                    HandLandmark(
                        handIndex = handIndex,
                        landmarkIndex = landmarkIndex,
                        x = landmark.x(),
                        y = landmark.y(),
                        z = landmark.z()
                    )
                )
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            _handLandmarks.emit(landmarks)
            Log.d(
                "HandDetection",
                "Detected ${landmarks.size} landmarks from ${result.landmarks().size} hands"
            )
        }
    }

    // 포즈 검출 결과 처리
    private fun processPoseResult(result: PoseLandmarkerResult) {
        val landmarks = mutableListOf<PoseLandmark>()

        result.landmarks().firstOrNull()?.let { poseLandmarks ->
            poseLandmarks.forEachIndexed { index, landmark ->
                landmarks.add(
                    PoseLandmark(
                        landmarkIndex = index,
                        x = landmark.x(),
                        y = landmark.y(),
                        z = landmark.z(),
                        visibility = if (result.landmarks().isNotEmpty())
                            result.landmarks()[0][index].visibility().orElse(0.0f)
                        else 0.0f
                    )
                )
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            _poseLandmarks.emit(landmarks)
        }
    }

    // 이미지 분석기 초기화
    private fun initializeImageAnalyzer() {
        imageAnalyzer = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalyzer.setAnalyzer(executor) { imageProxy ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastAnalysisTime >= analysisInterval) {
                processImageProxy(imageProxy)
                lastAnalysisTime = currentTime
            } else {
                imageProxy.close()
            }
        }
    }

    // ImageProxy를 Bitmap으로 변환하고 MediaPipe로 처리
    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        try {
            Log.d("HandDetection", "Processing image: ${imageProxy.width}x${imageProxy.height}")
            val bitmap = imageProxyToBitmap(imageProxy)
            if (bitmap != null) {
                Log.d("HandDetection", "Bitmap created: ${bitmap.width}x${bitmap.height}")
                val timestampMs = imageProxy.imageInfo.timestamp / 1000000 // 마이크로초를 밀리초로 변환

                val mpImage = BitmapImageBuilder(bitmap).build()

                // 손 탐지 실행
                if (_recordingState.value == RecordingState.OnRecord && isHandDetectionEnabled && handLandmarker != null) {
                    try {
                        val handResult = handLandmarker!!.detectForVideo(mpImage, timestampMs)
                        processHandsResult(handResult)
                    } catch (e: Exception) {
                        Log.e("HandDetection", "Error detecting hands", e)
                    }
                }

                // 포즈 탐지 실행
                if (_recordingState.value == RecordingState.OnRecord && isPoseDetectionEnabled && poseLandmarker != null) {
                    try {
                        val result = poseLandmarker!!.detectForVideo(mpImage, timestampMs)
                        processPoseResult(result)
                    } catch (e: Exception) {
                        Log.e("PoseDetection", "Error detecting pose", e)
                    }
                }
            } else {
                Log.e("HandDetection", "Failed to create bitmap")
            }
        } catch (e: Exception) {
            Log.e("HandDetection", "Error processing image", e)
        } finally {
            imageProxy.close()
        }
    }

    // ImageProxy를 Bitmap으로 변환
    @OptIn(ExperimentalGetImage::class)
    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap? {
        val image = imageProxy.image ?: return null

        return try {
            val yBuffer = image.planes[0].buffer // Y
            val vuBuffer = image.planes[2].buffer // VU

            val ySize = yBuffer.remaining()
            val vuSize = vuBuffer.remaining()

            val nv21 = ByteArray(ySize + vuSize)

            yBuffer.get(nv21, 0, ySize)
            vuBuffer.get(nv21, ySize, vuSize)

            val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
            val out = ByteArrayOutputStream()
            yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
            val imageBytes = out.toByteArray()

            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            // 회전 처리
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            if (rotationDegrees != 0) {
                val matrix = Matrix()
                matrix.postRotate(rotationDegrees.toFloat())
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }
        } catch (e: Exception) {
            Log.e("HandDetection", "Error converting ImageProxy to Bitmap", e)
            null
        }
    }

    override fun initialize(context: Context) {
        previewView = PreviewView(context)
        preview =
            Preview.Builder().build().also {
                it.surfaceProvider = previewView.surfaceProvider
            }
        cameraProvider = ProcessCameraProvider.getInstance(context)
        provider = cameraProvider.get()
        executor = Executors.newSingleThreadExecutor()
        this.context = context

        initializeVideo()
        initializeMediaPipe()
        initializeImageAnalyzer()
    }

    @OptIn(ExperimentalCamera2Interop::class)
    fun initializeVideo() {
        val qualitySelector = QualitySelector.fromOrderedList(
            listOf(Quality.UHD, Quality.FHD, Quality.HD, Quality.SD),
            FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
        )

        val recorder = Recorder.Builder()
            .setExecutor(executor).setQualitySelector(qualitySelector)
            .build()
        videoCapture = VideoCapture.withOutput(recorder)

        val path =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/cameraX")
        if (!path.exists()) path.mkdirs()
        val name = "" + SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS", Locale.KOREA
        ).format(System.currentTimeMillis()) + ".mp4"

        mediaStoreOutput = MediaStoreOutputOptions.Builder(
            context.contentResolver,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        )
            .setContentValues(ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, name)
            })
            .build()
    }

    override fun startCamera(
        lifecycleOwner: LifecycleOwner,
    ) {
        unBindCamera()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(_facing.value)
            .build()

        cameraProvider.addListener({
            CoroutineScope(Dispatchers.Main).launch {
                camera = provider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    videoCapture,
                    imageAnalyzer
                )
            }
        }, executor)
    }

    private fun enablePoseDetection() {
        isPoseDetectionEnabled = true
    }

    private fun disablePoseDetection() {
        isPoseDetectionEnabled = false
    }

    private fun enableHandDetection() {
        isHandDetectionEnabled = true
    }

    private fun disableHandDetection() {
        isHandDetectionEnabled = false
    }

    // 리소스 정리
    fun cleanup() {
        handLandmarker?.close()
        poseLandmarker?.close()
        executor.shutdown()
    }

    @SuppressLint("MissingPermission")
    override fun startRecordVideo() {
        Log.e("record", "start")
        try {
            recording = videoCapture.output
                .prepareRecording(context, mediaStoreOutput)
                .start(ContextCompat.getMainExecutor(context)) {
                    CoroutineScope(Dispatchers.Default + SupervisorJob()).launch {
                        with(it.recordingStats) {
                            _recordingInfo.emit(
                                RecordingInfo(
                                    duration = recordedDurationNanos,
                                    sizeByte = numBytesRecorded
                                )
                            )
                        }
                    }
                }
            _recordingState.value = RecordingState.OnRecord
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun stopRecordVideo() {
        Log.e("record", "end")
        recording.stop()
        _recordingState.value = RecordingState.Idle
    }

    override fun flipCameraFacing() {
        if (_facing.value == CameraSelector.LENS_FACING_BACK) {
            _facing.value = CameraSelector.LENS_FACING_FRONT
        } else {
            _facing.value = CameraSelector.LENS_FACING_BACK
        }
    }

    override fun unBindCamera() {
        if (::provider.isInitialized) {
            provider.unbindAll()
        }
    }

    override fun getPreviewView(): PreviewView = previewView
    override fun getFacingState(): StateFlow<Int> = _facing.asStateFlow()
    override fun getRecordingState(): StateFlow<RecordingState> = _recordingState.asStateFlow()
    override fun getRecordingInfo(): SharedFlow<RecordingInfo> = _recordingInfo.asSharedFlow()

}