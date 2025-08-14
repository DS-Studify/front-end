package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.request.RequestRecordDto
import com.ds.studify.core.data.dto.response.BaseResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.POST

interface StudyService {

    @POST("/record")
    suspend fun postRecord(
        @Body request: RequestRecordDto
    ): BaseResponse<JsonElement?>
}