package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.AnalysisEntity
import com.ds.studify.core.domain.entity.TimeEntry
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseAnalysisDto(
    @SerialName("studyRecordId")
    val studyRecordId: Int,
    @SerialName("studyDate")
    val studyDate: String,
    @SerialName("startTime")
    val startTime: String,
    @SerialName("endTime")
    val endTime: String,
    @SerialName("recordTime")
    val recordTime: String,
    @SerialName("recordRatio")
    val recordRatio: Int,
    @SerialName("actualStudyTime")
    val actualStudyTime: String,
    @SerialName("timeLog")
    val timeLog: Map<String, List<TimeLogEntryDto>>,
    @SerialName("aiFeedback")
    val aiFeedback: String
) {
    fun toEntity() = AnalysisEntity(
        studyRecordId = this.studyRecordId,
        studyDate = this.studyDate,
        startTime = this.startTime,
        endTime = this.endTime,
        recordTime = this.recordTime,
        recordRatio = this.recordRatio,
        actualStudyTime = this.actualStudyTime,
        timeLog = this.timeLog.toEntityTimeLog(),
        aiFeedback = this.aiFeedback
    )

    fun Map<String, List<TimeLogEntryDto>>.toEntityTimeLog(): Map<String, List<TimeEntry>> =
        mapValues { entry -> entry.value.map { it.toEntity() } }
}


