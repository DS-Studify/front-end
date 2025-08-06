package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.AuthDataSource
import com.ds.studify.core.data.dto.request.toDto
import com.ds.studify.core.data.repository.AuthRepository
import com.ds.studify.core.domain.entity.LoginEntity
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
}