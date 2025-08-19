package com.ds.studify.core.domain.entity

data class CalendarDailyEntity(
    val date: String,
    val totalStudyTime: Int,
    val focusTime: Int,
    val timeRanges: List<StudyRecord>
) {
    data class StudyRecord(
        val studyRecordId: Long,
        val start: String,
        val end: String
    )
}