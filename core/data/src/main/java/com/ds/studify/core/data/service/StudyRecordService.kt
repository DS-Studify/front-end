package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseCalendarDailyDto
import com.ds.studify.core.data.dto.response.ResponseCalendarMonthlyDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import retrofit2.http.GET
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
}