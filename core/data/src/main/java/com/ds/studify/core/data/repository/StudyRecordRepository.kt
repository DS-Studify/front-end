package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.CalendarDailyEntity
import com.ds.studify.core.domain.entity.CalendarMonthlyEntity
import com.ds.studify.core.domain.entity.HomeEntity

interface StudyRecordRepository {
    suspend fun getHome(): Result<HomeEntity>

    suspend fun getCalendarMonthly(month: String): Result<CalendarMonthlyEntity>

    suspend fun getCalendarDaily(date: String): Result<CalendarDailyEntity>
}