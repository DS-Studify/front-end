package com.ds.studify.core.data.repository

interface TokenRepository {

    fun getAccessToken(): String

    fun setAccessToken(accessToken: String)

    fun clearToken()
}