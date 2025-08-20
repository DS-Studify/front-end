package com.ds.studify.core.domain.entity

data class CalendarMonthlyEntity(
    val year: Int,
    val month: Int,
    val calendar: List<CalendarInfo>
) {
    data class CalendarInfo(
        val date: String,
        val totalStudyTime: Int
    )
}