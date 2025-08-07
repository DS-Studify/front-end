package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.request.RequestLoginDto
import com.ds.studify.core.data.dto.request.RequestReissueToken
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.dto.response.ResponseLoginDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/auth/login")
    suspend fun postLogin(
        @Body request: RequestLoginDto
    ): BaseResponse<ResponseLoginDto>

    @POST("/auth/refresh")
    suspend fun postRefresh(
        @Body request: RequestReissueToken
    ): BaseResponse<ResponseLoginDto>

}