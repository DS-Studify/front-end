package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.CalendarEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCalendarDto(
    @SerialName("year")
    val year: Int,
    @SerialName("month")
    val month: Int,
    @SerialName("calendar")
    val calendar: List<CalendarInfo>,
    @SerialName("detail")
    val detail: DetailInfo
) {
    @Serializable
    data class CalendarInfo(
        @SerialName("date")
        val date: String,
        @SerialName("totalStudyTime")
        val totalStudyTime: Int
    )

    @Serializable
    data class DetailInfo(
        @SerialName("date")
        val date: String,
        @SerialName("totalStudyTime")
        val totalStudyTime: Int,
        @SerialName("focusTime")
        val focusTime: Int,
        @SerialName("timeRanges")
        val timeRanges: List<Study>
    ) {
        @Serializable
        data class Study(
            @SerialName("studyRecordId")
            val studyRecordId: Long,
            @SerialName("start")
            val start: String,
            @SerialName("end")
            val end: String
        )
    }

    fun toEntity() = CalendarEntity(
        year = this.year,
        month = this.month,
        calendar = this.calendar.map {
            CalendarEntity.CalendarInfo(
                date = it.date,
                totalStudyTime = it.totalStudyTime
            )
        },
        detail = this.detail.let {
            CalendarEntity.DetailInfo(
                date = it.date,
                totalStudyTime = it.totalStudyTime,
                focusTime = it.focusTime,
                timeRanges = it.timeRanges.map { range ->
                    CalendarEntity.DetailInfo.Study(
                        studyRecordId = range.studyRecordId,
                        start = range.start,
                        end = range.end
                    )
                }
            )
        }
    )
}