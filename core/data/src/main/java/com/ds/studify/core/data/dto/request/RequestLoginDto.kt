package com.ds.studify.core.data.dto.request

import com.ds.studify.core.domain.entity.LoginEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestLoginDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)

fun LoginEntity.toDto() = RequestLoginDto(
    email = email,
    password = password
)