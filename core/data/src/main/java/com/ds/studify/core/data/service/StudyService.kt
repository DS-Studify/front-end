package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.request.RequestRecordDto
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseAnalysisDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StudyService {

    @POST("/record")
    suspend fun postRecord(
        @Body request: RequestRecordDto
    ): BaseResponse<Long>

    @GET("/record/{studyRecordId}/result")
    suspend fun getAnalysis(
        @Path("studyRecordId") studyRecordId: Long
    ): BaseResponse<ResponseAnalysisDto>
}