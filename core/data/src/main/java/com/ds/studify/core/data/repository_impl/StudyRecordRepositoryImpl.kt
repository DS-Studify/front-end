package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.StudyRecordDataSource
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.domain.entity.CalendarDailyEntity
import com.ds.studify.core.domain.entity.CalendarMonthlyEntity
import com.ds.studify.core.domain.entity.HomeEntity
import javax.inject.Inject

class StudyRecordRepositoryImpl @Inject constructor(
    private val studyRecordDataSource: StudyRecordDataSource
) : StudyRecordRepository {

    override suspend fun getHome(): Result<HomeEntity> =
        runCatching {
            studyRecordDataSource.getHome().data.toEntity()
        }

    override suspend fun getCalendarMonthly(month: String): Result<CalendarMonthlyEntity> =
        runCatching {
            studyRecordDataSource.getCalendarMonthly(month = month).data.toEntity()
        }

    override suspend fun getCalendarDaily(date: String): Result<CalendarDailyEntity> =
        runCatching {
            studyRecordDataSource.getCalendarDaily(date = date).data.toEntity()
        }
}