package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.LoginEntity
import com.ds.studify.core.domain.entity.SignupEntity
import com.ds.studify.core.domain.entity.TokenEntity

interface AuthRepository {
    suspend fun postLogin(loginData: LoginEntity): Result<TokenEntity>

    suspend fun postSendVerification(email: String): Result<String>
    suspend fun postReverify(email: String): Result<String>
    suspend fun postCheckVerification(email: String, code: String): Result<Boolean>
    suspend fun postRegister(entity: SignupEntity): Result<Unit>
}