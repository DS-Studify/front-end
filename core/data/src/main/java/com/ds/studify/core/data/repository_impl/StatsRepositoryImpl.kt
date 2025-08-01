package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.model.StudyTimeRange
import com.ds.studify.core.data.repository.StatsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class StatsRepositoryImpl @Inject constructor() : StatsRepository {

    private var todayStudyTime = MutableStateFlow(0L)

    override val todayStudyTimeStream: Flow<Long>
        get() = todayStudyTime.asStateFlow()

    private val monthlyStudyHistoryCache = mutableMapOf<Pair<Int, Int>, List<String>>()

    private fun generateDailySaving(): String {
        return if (Random.nextBoolean()) {
            ""
        } else {
            "${Random.nextInt(0, 4)}H ${Random.nextInt(0, 60)}M"
        }
    }

    override suspend fun getStudyHistoryInMonth(year: Int, month: Int): List<String> {
        if (monthlyStudyHistoryCache[year to month] == null) {
            val data = mutableListOf<String>()
            val date = LocalDate.now()
            val isThisMonth = date.year == year && date.monthValue == month
            val day = date.dayOfMonth

            if (isThisMonth) {
                repeat(day - 1) {
                    data.add(generateDailySaving())
                }
            } else {
                val maxDayOfYear = LocalDate.of(year, month, 1).lengthOfMonth()
                repeat(maxDayOfYear) {
                    when (monthlyStudyHistoryCache.size) {
                        1 -> data.add("")
                        2 -> data.add("1H 20M")
                        else -> data.add(generateDailySaving())
                    }
                }
            }
            monthlyStudyHistoryCache[year to month] = data
        }

        return monthlyStudyHistoryCache[year to month]!!
    }

    override suspend fun getDailyFocusTime(year: Int, month: Int): String {
        val hours = Random.nextInt(0, 4)
        val minutes = Random.nextInt(0, 60)

        return "${hours}H ${minutes}M"
    }

    override suspend fun getDailyStudyTime(year: Int, month: Int, day: Int): String {
        val key = year to month
        val studyList = monthlyStudyHistoryCache[key]

        return studyList?.getOrNull(day - 1) ?: "0H 0M"
    }

    override suspend fun getDailyStudyTimeLine(year: Int, month: Int): List<StudyTimeRange> {
        return listOf(
            StudyTimeRange(1, "10:00", "13:00"),
            StudyTimeRange(2, "14:30", "18:33"),
            StudyTimeRange(3, "19:40", "23:04")
        )
    }
}