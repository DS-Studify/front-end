package com.ds.studify.core.data.dto.request

import com.ds.studify.core.domain.entity.CameraEntity
import com.ds.studify.core.domain.entity.TimeLog
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestRecordDto(
    @SerialName("date")
    val date: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("timeLog")
    val timeLog: Map<String, List<TimeLogEntry>>
)

@Serializable
data class TimeLogEntry(
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String
)

fun CameraEntity.toDto(): RequestRecordDto =
    RequestRecordDto(
        date = date,
        startTime = startTime,
        endTime = endTime,
        timeLog = timeLog.mapValues { (_, ranges: List<TimeLog>) ->
            ranges.map { TimeLogEntry(startTime = it.startTime, endTime = it.endTime) }
        }
    )

