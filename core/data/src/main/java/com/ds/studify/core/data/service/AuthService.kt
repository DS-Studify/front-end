package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.request.RequestLoginDto
import com.ds.studify.core.data.dto.request.RequestRegisterDto
import com.ds.studify.core.data.dto.request.RequestReissueToken
import com.ds.studify.core.data.dto.request.RequestSendVerificationDto
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseLoginDto
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/auth/login")
    suspend fun postLogin(
        @Body request: RequestLoginDto
    ): BaseResponse<ResponseLoginDto>

    @POST("/auth/refresh")
    suspend fun postRefresh(
        @Body request: RequestReissueToken
    ): BaseResponse<ResponseLoginDto>

    @POST("auth/register")
    suspend fun postRegister(
        @Body request: RequestRegisterDto
    ): BaseResponse<JsonElement?>

    @POST("auth/send-verification")
    suspend fun postSendVerification(
        @Body request: RequestSendVerificationDto
    ): BaseResponse<String>

    @POST("auth/reverify")
    suspend fun postReverify(
        @Query("email") email: String
    ): BaseResponse<String>

    @POST("auth/check-verification")
    suspend fun postCheckVerification(
        @Query("email") email: String,
        @Query("code") code: String
    ): BaseResponse<Boolean>
}