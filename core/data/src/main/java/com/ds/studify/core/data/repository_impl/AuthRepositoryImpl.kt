package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.AuthDataSource
import com.ds.studify.core.data.dto.request.RequestSendVerificationDto
import com.ds.studify.core.data.dto.request.toDto
import com.ds.studify.core.data.repository.AuthRepository
import com.ds.studify.core.domain.entity.LoginEntity
import com.ds.studify.core.domain.entity.SignupEntity
import com.ds.studify.core.domain.entity.TokenEntity
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : AuthRepository {

    override suspend fun postLogin(loginData: LoginEntity): Result<TokenEntity> =
        runCatching {
            authDataSource.postLogin(
                request = loginData.toDto()
            ).data.toEntity()
        }

    override suspend fun postSendVerification(email: String): Result<String> =
        runCatching {
            authDataSource.postSendVerification(
                request = RequestSendVerificationDto(email = email)
            ).data
        }

    override suspend fun postReverify(email: String): Result<String> =
        runCatching {
            authDataSource.postReverify(email = email).data
        }

    override suspend fun postCheckVerification(email: String, code: String): Result<Boolean> =
        runCatching {
            authDataSource.postCheckVerification(email = email, code = code).data
        }

    override suspend fun postRegister(entity: SignupEntity): Result<Unit> =
        runCatching {
            authDataSource.postRegister(
                request = entity.toDto()
            )
            Unit
        }
}