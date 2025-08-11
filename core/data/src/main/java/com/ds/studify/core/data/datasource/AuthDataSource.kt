package com.ds.studify.core.data.datasource

import com.ds.studify.core.data.dto.request.RequestLoginDto
import com.ds.studify.core.data.dto.request.RequestRegisterDto
import com.ds.studify.core.data.dto.request.RequestReissueToken
import com.ds.studify.core.data.dto.request.RequestSendVerificationDto
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseLoginDto
import com.ds.studify.core.data.service.AuthService
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject

class AuthDataSource @Inject constructor(
    private val authService: AuthService
) {
    suspend fun postLogin(request: RequestLoginDto): BaseResponse<ResponseLoginDto> =
        authService.postLogin(request = request)

    suspend fun postTokenReissue(request: RequestReissueToken): BaseResponse<ResponseLoginDto> =
        authService.postRefresh(request = request)

    suspend fun postRegister(request: RequestRegisterDto): BaseResponse<JsonElement?> =
        authService.postRegister(request = request)

    suspend fun postSendVerification(request: RequestSendVerificationDto): BaseResponse<String> =
        authService.postSendVerification(request = request)

    suspend fun postReverify(email: String): BaseResponse<String> =
        authService.postReverify(email = email)

    suspend fun postCheckVerification(email: String, code: String): BaseResponse<String> =
        authService.postCheckVerification(email = email, code = code)
}