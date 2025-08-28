package com.ds.studify.core.data.datasource

import com.ds.studify.core.data.dto.request.RequestRecordDto
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseAnalysisDto
import com.ds.studify.core.data.service.StudyService
import javax.inject.Inject

class StudyDataSource @Inject constructor(
    private val studyService: StudyService
) {
    suspend fun postRecord(request: RequestRecordDto): BaseResponse<Long> =
        studyService.postRecord(request = request)

    suspend fun getAnalysis(studyRecordId: Long): BaseResponse<ResponseAnalysisDto> =
        studyService.getAnalysis(studyRecordId)
}