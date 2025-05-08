package com.ds.studify.convention

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object ProjectConfiguration {
    const val COMPILE_SDK = 34
    const val TARGET_SDK = 34
    const val MIN_SDK = 27
    val SOURCE_COMPATIBILITY = JavaVersion.VERSION_17
    val TARGET_COMPATIBILITY = JavaVersion.VERSION_17
    val KOTLIN_COMPILER_JVM_TARGET = JvmTarget.JVM_17
}