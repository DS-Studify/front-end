package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import retrofit2.http.GET

interface StudyRecordService {

    @GET("/home")
    suspend fun getHome(): BaseResponse<ResponseHomeDto>
}