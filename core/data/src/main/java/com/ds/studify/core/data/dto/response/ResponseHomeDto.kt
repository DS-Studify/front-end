package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.HomeEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHomeDto(
    @SerialName("nickName")
    val nickName: String,
    @SerialName("todayStudyTime")
    val todayStudyTime: Int
) {
    fun toEntity() = HomeEntity(
        nickName = this.nickName,
        todayStudyTime = this.todayStudyTime
    )
}