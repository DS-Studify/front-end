package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.TimeEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeLogEntryDto(
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String
) {
    fun toEntity() = TimeEntry(
        startTime = this.startTime,
        endTime = this.endTime
    )
}
