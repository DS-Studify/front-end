package com.ds.studify.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val email: String,
    val nickname: String
)