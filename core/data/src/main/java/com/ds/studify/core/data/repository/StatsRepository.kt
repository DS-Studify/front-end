package com.ds.studify.core.data.repository

import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    val todayStudyTimeStream: Flow<Long>
}