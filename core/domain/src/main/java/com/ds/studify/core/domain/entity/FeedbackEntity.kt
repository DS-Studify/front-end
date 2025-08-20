package com.ds.studify.core.domain.entity

data class FeedbackEntity(
    val studyRecordId: Long,
    val studyDate: String,
    val startTime: String,
    val endTime: String,
    val actualStudyTime: Int,
    val timeLog: Map<String, List<TimeEntry>>,
    val aiFeedback: String
)