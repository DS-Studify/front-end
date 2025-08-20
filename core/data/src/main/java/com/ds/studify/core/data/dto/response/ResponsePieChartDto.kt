package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.PieChartEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponsePieChartDto(
    @SerialName("label")
    val label: String,
    @SerialName("ratio")
    val ratio: Int,
    @SerialName("time")
    val time: Int
) {
    fun toEntity() = PieChartEntity(
        label = this.label,
        time = this.time
    )
}

fun List<ResponsePieChartDto>.toEntity(): List<PieChartEntity> =
    this.map { it.toEntity() }