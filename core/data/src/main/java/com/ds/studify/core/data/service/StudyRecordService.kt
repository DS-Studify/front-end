package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseAnalysisDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import retrofit2.http.Path
import com.ds.studify.core.data.dto.response.ResponseCalendarDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import retrofit2.http.GET
import retrofit2.http.Query

interface StudyRecordService {

    @GET("/home")
    suspend fun getHome(): BaseResponse<ResponseHomeDto>

    @GET("/record/{studyRecordId}/result")
    suspend fun getAnalysis(
        @Path("studyRecordId") studyRecordId: Int
    ): BaseResponse<ResponseAnalysisDto>

    @GET("/statistics/calendar")
    suspend fun getCalendar(
        @Query("date") date: String
    ): BaseResponse<ResponseCalendarDto>

}