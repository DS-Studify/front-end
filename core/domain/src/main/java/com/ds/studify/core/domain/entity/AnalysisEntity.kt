package com.ds.studify.core.domain.entity

data class AnalysisEntity(
    val studyRecordId: Long,
    val studyDate: String,
    val startTime: String,
    val endTime: String,
    val recordTime: String,
    val recordRatio: Int,
    val actualStudyTime: String,
    val timeLog: Map<String, List<TimeEntry>>,
    val aiFeedback: String
)

data class TimeEntry(
    val startTime: String,
    val endTime: String
)