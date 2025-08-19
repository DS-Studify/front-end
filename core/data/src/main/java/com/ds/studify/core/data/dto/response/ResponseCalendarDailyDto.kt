package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.CalendarDailyEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCalendarDailyDto(
    @SerialName("date")
    val date: String,
    @SerialName("totalStudyTime")
    val totalStudyTime: Int,
    @SerialName("focusTime")
    val focusTime: Int,
    @SerialName("timeRanges")
    val timeRanges: List<StudyRecord>
) {
    @Serializable
    data class StudyRecord(
        @SerialName("studyRecordId")
        val studyRecordId: Long,
        @SerialName("start")
        val start: String,
        @SerialName("end")
        val end: String
    )

    fun toEntity() = CalendarDailyEntity(
        date = this.date,
        totalStudyTime = this.totalStudyTime,
        focusTime = this.focusTime,
        timeRanges = this.timeRanges.map {
            CalendarDailyEntity.StudyRecord(
                studyRecordId = it.studyRecordId,
                start = it.start,
                end = it.end
            )
        }
    )
}