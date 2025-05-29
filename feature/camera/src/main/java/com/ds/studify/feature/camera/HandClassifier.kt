package com.ds.studify.feature.camera

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class HandClassifier(private val context: Context) {

    companion object {
        private const val HAND_MODEL_FILE = "hand_model_with_scaler.tflite"
    }

    private val interpreter: Interpreter

    init {
        val model = loadModelFile()
        interpreter = Interpreter(model)
    }

    private fun loadModelFile(): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(HAND_MODEL_FILE)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel

        return fileChannel.map(
            FileChannel.MapMode.READ_ONLY,
            fileDescriptor.startOffset,
            fileDescriptor.declaredLength
        )
    }

    fun predict(input: FloatArray): Float {
        val output = Array(1) { FloatArray(1) }
        interpreter.run(arrayOf(input), output)
        return output[0][0]
    }
}