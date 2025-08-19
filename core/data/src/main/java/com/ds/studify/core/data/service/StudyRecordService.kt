package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseAnalysisDto
import com.ds.studify.core.data.dto.response.ResponseHomeDto
import retrofit2.http.GET
import retrofit2.http.Path

interface StudyRecordService {

    @GET("/home")
    suspend fun getHome(): BaseResponse<ResponseHomeDto>

    @GET("/record/{studyRecordId}/result")
    suspend fun getAnalysis(
        @Path("studyRecordId") studyRecordId: Int
    ): BaseResponse<ResponseAnalysisDto>
}