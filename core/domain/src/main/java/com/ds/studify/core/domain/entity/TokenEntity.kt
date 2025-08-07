package com.ds.studify.core.domain.entity

data class TokenEntity(
    val accessToken: String,
    val refreshToken: String
)
