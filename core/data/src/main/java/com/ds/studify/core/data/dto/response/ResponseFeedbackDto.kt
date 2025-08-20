package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.FeedbackEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseFeedbackDto(
    @SerialName("studyRecordId")
    val studyRecordId: Long,
    @SerialName("studyDate")
    val studyDate: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("actualStudyTime")
    val actualStudyTime: Int,
    @SerialName("timeLog")
    val timeLog: Map<String, List<TimeLog>>,
    @SerialName("aiFeedback")
    val aiFeedback: String
) {
    @Serializable
    data class TimeLog(
        @SerialName("startTime")
        val startTime: String,
        @SerialName("endTime")
        val endTime: String
    )

    fun toEntity() = FeedbackEntity(
        studyRecordId = this.studyRecordId,
        studyDate = this.studyDate,
        startTime = this.startTime,
        endTime = this.endTime,
        actualStudyTime = this.actualStudyTime,
        timeLog = this.timeLog.mapValues { (_, list) ->
            list.map { FeedbackEntity.TimeLog(startTime = it.startTime, endTime = it.endTime) }
        },
        aiFeedback = this.aiFeedback
    )
}