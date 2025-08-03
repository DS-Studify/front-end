package com.ds.studify.feature.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

data class AnalysisState(
    val sampleData: String = ""
)

data class Segment(
    val startRatio: Float,
    val height: Float
)

data class BarData(
    val stateId: Int,
    val segments: List<Segment>
)

data class TimeRange(
    val startTime: LocalDateTime,
    val endTime: LocalDateTime
)

@HiltViewModel
class AnalysisViewModel @Inject constructor() : ViewModel(), ContainerHost<AnalysisState, Nothing> {

    override val container = container<AnalysisState, Nothing>(
        initialState = AnalysisState()
    )

    private val _barDataList = MutableStateFlow<List<BarData>>(emptyList())
    val barDataList: StateFlow<List<BarData>> = _barDataList

    private val _studyTimeRange = MutableStateFlow<Pair<LocalDateTime, LocalDateTime>?>(null)
    val studyTimeRange: StateFlow<Pair<LocalDateTime, LocalDateTime>?> = _studyTimeRange

    init {
        val jsonString = """
            {
            "startTime":"2025-08-01T23:10:44",
            "endTime":"2025-08-01T23:11:13",
            "timeLog":{
                "1":[
                    {"startTime":"2025-08-01T23:10:52","endTime":"2025-08-01T23:11:10"}
                ],
                "2":[
                    {"startTime":"2025-08-01T23:10:52","endTime":"2025-08-01T23:10:59"},
                    {"startTime":"2025-08-01T23:11:04","endTime":"2025-08-01T23:11:10"}
                ],
                "3":[
                    {"startTime":"2025-08-01T23:10:59","endTime":"2025-08-01T23:11:04"}
                ],
                "4":[],
                "5":[
                    {"startTime":"2025-08-01T23:10:44","endTime":"2025-08-01T23:10:49"}
                ],
                "6":[
                    {"startTime":"2025-08-01T23:10:49","endTime":"2025-08-01T23:10:52"},
                    {"startTime":"2025-08-01T23:11:10","endTime":"2025-08-01T23:11:13"}
                ]
            }
        }
        """.trimIndent()

        val (studyStart, studyEnd, timeLogs) = parseJson(jsonString)
        _studyTimeRange.value = studyStart to studyEnd
        getBarRatioInSeconds(timeLogs, studyStart, studyEnd)

    }

    fun parseJson(
        jsonString: String
    ): Triple<LocalDateTime, LocalDateTime, Map<Int, List<TimeRange>>> {
        val jsonObject = JSONObject(jsonString)
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        val studyStart = LocalDateTime.parse(jsonObject.getString("startTime"), formatter)
        val studyEnd = LocalDateTime.parse(jsonObject.getString("endTime"), formatter)

        val timeLogObject = jsonObject.getJSONObject("timeLog")
        val result = mutableMapOf<Int, List<TimeRange>>()
        timeLogObject.keys().forEach { key ->
            val jsonArray = timeLogObject.getJSONArray(key)
            val ranges = mutableListOf<TimeRange>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val start = LocalDateTime.parse(obj.getString("startTime"), formatter)
                val end = LocalDateTime.parse(obj.getString("endTime"), formatter)
                ranges.add(TimeRange(start, end))
            }
            result[key.toInt()] = ranges
        }
        return Triple(studyStart, studyEnd, result)
    }

    fun getBarRatioInSeconds(
        timeLogs: Map<Int, List<TimeRange>>,
        start: LocalDateTime,
        end: LocalDateTime
    ) {
        viewModelScope.launch {
            val totalSeconds = Duration.between(start, end).seconds.toFloat()

            val barList = timeLogs.map { (stateId, logs) ->
                val segments = logs.map { log ->
                    val startRatio = Duration.between(start, log.startTime).seconds / totalSeconds
                    val endRatio = Duration.between(start, log.endTime).seconds / totalSeconds
                    val height = endRatio - startRatio
                    Segment(startRatio, height)
                }
                BarData(stateId, segments)
            }
            _barDataList.value = barList
        }
    }

}