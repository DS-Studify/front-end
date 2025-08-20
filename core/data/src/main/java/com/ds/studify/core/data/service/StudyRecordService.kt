package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseCalendarDailyDto
import com.ds.studify.core.data.dto.response.ResponseCalendarMonthlyDto
import com.ds.studify.core.data.dto.response.ResponseFeedbackDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import com.ds.studify.core.data.dto.response.ResponsePieChartDto
import com.ds.studify.core.data.dto.response.ResponseAnalysisDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StudyRecordService {

    @GET("/home")
    suspend fun getHome(): BaseResponse<ResponseHomeDto>

    @GET("/statistics/calendar/monthly")
    suspend fun getCalendarMonthly(
        @Query("month") month: String
    ): BaseResponse<ResponseCalendarMonthlyDto>

    @GET("/statistics/calendar/daily")
    suspend fun getCalendarDaily(
        @Query("date") date: String
    ): BaseResponse<ResponseCalendarDailyDto>

    @GET("/record/{studyRecordId}/feedback")
    suspend fun getFeedback(
        @Path("studyRecordId") studyRecordId: Long
    ): BaseResponse<ResponseFeedbackDto>

    @GET("/record/{studyRecordId}/pie-chart")
    suspend fun getPieChart(
        @Path("studyRecordId") studyRecordId: Long,
        @Query("tab") tab: String
    ): BaseResponse<List<ResponsePieChartDto>>

    @GET("/record/{studyRecordId}/result")
    suspend fun getAnalysis(
        @Path("studyRecordId") studyRecordId: Int
    ): BaseResponse<ResponseAnalysisDto>
}