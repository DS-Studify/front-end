package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.CalendarEntity
import com.ds.studify.core.domain.entity.HomeEntity

interface StudyRecordRepository {
    suspend fun getHome(): Result<HomeEntity>

    suspend fun getCalendar(date: String): Result<CalendarEntity>
}