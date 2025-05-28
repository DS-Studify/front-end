package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepositoryImpl @Inject constructor() : StatsRepository {

    private var todayStudyTime = MutableStateFlow(0L)

    override val todayStudyTimeStream: Flow<Long>
        get() = todayStudyTime.asStateFlow()
}