package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.StudyRecordDataSource
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.AnalysisEntity
import com.ds.studify.core.domain.entity.CalendarEntity
import com.ds.studify.core.domain.entity.HomeEntity
import javax.inject.Inject

class StudyRecordRepositoryImpl @Inject constructor(
    private val studyRecordDataSource: StudyRecordDataSource
) : StudyRecordRepository {

    override suspend fun getHome(): Result<HomeEntity> =
        runCatching {
            studyRecordDataSource.getHome().data.toEntity()
        }

    override suspend fun getAnalysis(studyRecordId: Int): Result<AnalysisEntity> =
        runCatching {
            studyRecordDataSource.getAnalysis(studyRecordId).data.toEntity()
        }

    override suspend fun getCalendar(date: String): Result<CalendarEntity> =
        runCatching {
            studyRecordDataSource.getCalendar(date = date).data.toEntity()
        }
}