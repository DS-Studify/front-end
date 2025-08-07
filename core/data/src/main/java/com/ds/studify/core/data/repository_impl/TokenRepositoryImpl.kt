package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.TokenDataSource
import com.ds.studify.core.data.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {

    override fun getAccessToken(): String = tokenDataSource.accessToken
    override fun getRefreshToken(): String = tokenDataSource.refreshToken

    override fun setAccessToken(accessToken: String, refreshToken: String) {
        tokenDataSource.accessToken = accessToken
        tokenDataSource.refreshToken = refreshToken
    }

    override fun clearToken() {
        tokenDataSource.clearToken()
    }
}