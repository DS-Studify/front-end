package com.ds.studify.feature.camera

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class PoseClassifier(private val context: Context) {

    companion object {
        private const val POSE_MODEL_FILE = "pose_mlp_model.tflite"
    }

    private val interpreter: Interpreter

    init {
        val model = loadModelFile()
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(POSE_MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel

        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    fun predict(input: FloatArray): FloatArray {
        val output = Array(1) { FloatArray(6) } // 6가지 자세 클래스
        interpreter.run(arrayOf(input), output)
        return output[0]  // Softmax 결과 [6]
    }
}