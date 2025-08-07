package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.LoginEntity
import com.ds.studify.core.domain.entity.TokenEntity

interface AuthRepository {
    suspend fun postLogin(loginData: LoginEntity): Result<TokenEntity>
}