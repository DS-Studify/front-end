package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.CalendarMonthlyEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseCalendarMonthlyDto(
    @SerialName("year")
    val year: Int,
    @SerialName("month")
    val month: Int,
    @SerialName("calendar")
    val calendar: List<CalendarInfo>
) {
    @Serializable
    data class CalendarInfo(
        @SerialName("date")
        val date: String,
        @SerialName("totalStudyTime")
        val totalStudyTime: Int
    )

    fun toEntity() = CalendarMonthlyEntity(
        year = this.year,
        month = this.month,
        calendar = this.calendar.map {
            CalendarMonthlyEntity.CalendarInfo(
                date = it.date,
                totalStudyTime = it.totalStudyTime
            )
        }
    )
}