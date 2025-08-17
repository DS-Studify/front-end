package com.ds.studify.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestChangePasswordDto(
    val originPassword: String,
    val newPassword: String
)