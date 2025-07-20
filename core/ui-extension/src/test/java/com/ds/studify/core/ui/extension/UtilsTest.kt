package com.ds.studify.core.ui.extension

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsTest {
    @Test
    fun testFormatTime_HoursAndMinutes() {
        val input = 6 * 3600 + 45 * 60 // 6시간 45분 = 24300초
        val expected = "6시간 45분"
        assertEquals(expected, formatTimeInKorean(input))
    }

    @Test
    fun testFormatTime_OnlyMinutesAndSeconds() {
        val input = 1 * 60 + 30 // 1분 30초
        val expected = "1분 30초"
        assertEquals(expected, formatTimeInKorean(input))
    }

    @Test
    fun testFormatTime_OnlyMinutes() {
        val input = 3 * 60
        val expected = "3분"
        assertEquals(expected, formatTimeInKorean(input))
    }

    @Test
    fun testFormatTime_OnlySeconds() {
        val input = 59
        val expected = "59초"
        assertEquals(expected, formatTimeInKorean(input))
    }

    @Test
    fun testFormatTime_HoursOnly() {
        val input = 2 * 3600
        val expected = "2시간"
        assertEquals(expected, formatTimeInKorean(input))
    }

    @Test
    fun testFormatTime_Zero() {
        val input = 0
        val expected = "0초"
        assertEquals(expected, formatTimeInKorean(input))
    }
}