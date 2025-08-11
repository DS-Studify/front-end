package com.ds.studify.core.data.dto.request

import com.ds.studify.core.domain.entity.SignupEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestRegisterDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String,
    @SerialName("nickname")
    val nickname: String
)

fun SignupEntity.toDto() = RequestRegisterDto(
    email = email,
    password = password,
    nickname = nickname
)