package com.ds.studify.core.data.repository

interface TokenRepository {

    fun getAccessToken(): String

    fun getRefreshToken(): String

    fun setAccessToken(accessToken: String, refreshToken: String)

    fun clearToken()
}