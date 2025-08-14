package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.StudyDataSource
import com.ds.studify.core.data.dto.request.toDto
import com.ds.studify.core.data.repository.StudyRepository
import com.ds.studify.core.domain.entity.CameraEntity
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject

class StudyRepositoryImpl @Inject constructor(
    private val studyDataSource: StudyDataSource
): StudyRepository{
    override suspend fun postRecord(entity: CameraEntity): Result<JsonElement?> =
        runCatching {
            studyDataSource.postRecord(
                request = entity.toDto()
            ).data
    }
}