package com.ds.studify.core.data.dto.response

import com.ds.studify.core.domain.entity.TokenEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseLoginDto(
    @SerialName("token")
    val token: String
) {
    fun toEntity() = TokenEntity(
        token = this.token
    )
}