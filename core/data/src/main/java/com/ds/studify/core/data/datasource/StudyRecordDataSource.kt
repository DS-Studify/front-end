package com.ds.studify.core.data.datasource

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseCalendarDailyDto
import com.ds.studify.core.data.dto.response.ResponseCalendarMonthlyDto
import com.ds.studify.core.data.dto.response.ResponseFeedbackDto
import com.ds.studify.core.data.dto.response.ResponseAnalysisDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import com.ds.studify.core.data.dto.response.ResponsePieChartDto
import com.ds.studify.core.data.service.StudyRecordService
import javax.inject.Inject

class StudyRecordDataSource @Inject constructor(
    private val studyRecordService: StudyRecordService
) {
    suspend fun getHome(): BaseResponse<ResponseHomeDto> =
        studyRecordService.getHome()

    suspend fun getCalendarMonthly(month: String): BaseResponse<ResponseCalendarMonthlyDto> =
        studyRecordService.getCalendarMonthly(month = month)

    suspend fun getCalendarDaily(date: String): BaseResponse<ResponseCalendarDailyDto> =
        studyRecordService.getCalendarDaily(date = date)

    suspend fun getFeedback(studyRecordId: Long): BaseResponse<ResponseFeedbackDto> =
        studyRecordService.getFeedback(studyRecordId = studyRecordId)

    suspend fun getPieChart(studyRecordId: Long, tab: String): BaseResponse<List<ResponsePieChartDto>> =
        studyRecordService.getPieChart(studyRecordId = studyRecordId, tab = tab)
        
    suspend fun getAnalysis(
        studyRecordId: Int
    ): BaseResponse<ResponseAnalysisDto> =
        studyRecordService.getAnalysis(studyRecordId)
}