package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.AnalysisEntity
import com.ds.studify.core.domain.entity.CalendarEntity
import com.ds.studify.core.domain.entity.HomeEntity

interface StudyRecordRepository {
    suspend fun getHome(): Result<HomeEntity>

    suspend fun getAnalysis(studyRecordId: Int): Result<AnalysisEntity>

    suspend fun getCalendar(date: String): Result<CalendarEntity>

}