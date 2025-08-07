package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.TokenEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String
) {
    fun toEntity() = TokenEntity(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken
    )
}