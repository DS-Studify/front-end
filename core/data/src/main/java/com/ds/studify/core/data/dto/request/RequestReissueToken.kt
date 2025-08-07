package com.ds.studify.core.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestReissueToken(
    @SerialName("refreshToken")
    val refreshToken: String
)