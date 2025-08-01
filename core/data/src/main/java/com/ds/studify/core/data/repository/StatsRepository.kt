package com.ds.studify.core.data.repository

import com.ds.studify.core.data.model.StudyTimeRange
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    val todayStudyTimeStream: Flow<Long>

    suspend fun getStudyHistoryInMonth(year: Int, month: Int): List<String>

    suspend fun getDailyFocusTime(year: Int, month: Int): String

    suspend fun getDailyStudyTime(year: Int, month: Int, day: Int): String

    suspend fun getDailyStudyTimeLine(year: Int, month: Int): List<StudyTimeRange>
}