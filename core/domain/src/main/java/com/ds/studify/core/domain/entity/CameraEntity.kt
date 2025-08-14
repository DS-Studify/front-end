package com.ds.studify.core.domain.entity

data class CameraEntity(
    val date: String,
    val startTime: String,
    val endTime: String,
    val timeLog: Map<String, List<TimeLog>>
)

data class TimeLog(
    val startTime: String,
    val endTime: String
)