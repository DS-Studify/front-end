package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.AnalysisEntity
import com.ds.studify.core.domain.entity.CameraEntity

interface StudyRepository {
    suspend fun postRecord(entity: CameraEntity): Result<Long>

    suspend fun getAnalysis(studyRecordId: Long): Result<AnalysisEntity>
}