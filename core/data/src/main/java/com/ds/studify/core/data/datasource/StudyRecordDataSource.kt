package com.ds.studify.core.data.datasource

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseCalendarDailyDto
import com.ds.studify.core.data.dto.response.ResponseCalendarMonthlyDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
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
}