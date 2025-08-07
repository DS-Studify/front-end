package com.ds.studify.core.data.datasource

import com.ds.studify.core.data.dto.request.RequestLoginDto
import com.ds.studify.core.data.dto.request.RequestReissueToken
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseLoginDto
import com.ds.studify.core.data.service.AuthService
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService
) {
    suspend fun postLogin(request: RequestLoginDto): BaseResponse<ResponseLoginDto> =
        authService.postLogin(request = request)

    suspend fun postTokenReissue(request: RequestReissueToken): BaseResponse<ResponseLoginDto> =
        authService.postRefresh(request = request)

}