package com.ds.studify.core.domain.entity

data class CalendarEntity(
    val year: Int,
    val month: Int,
    val calendar: List<CalendarInfo>,
    val detail: DetailInfo
) {
    data class CalendarInfo(
        val date: String,
        val totalStudyTime: Int
    )

    data class DetailInfo(
        val date: String,
        val totalStudyTime: Int,
        val focusTime: Int,
        val timeRanges: List<Study>
    ) {
        data class Study(
            val studyRecordId: Long,
            val start: String,
            val end: String
        )
    }
}