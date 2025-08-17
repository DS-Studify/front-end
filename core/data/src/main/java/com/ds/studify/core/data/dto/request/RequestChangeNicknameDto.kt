package com.ds.studify.core.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestChangeNicknameDto(
    @SerialName("newNickname")
    val nickname: String
)