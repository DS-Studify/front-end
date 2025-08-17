package com.ds.studify.core.data.service

import com.ds.studify.core.data.dto.request.ProfileDto
import com.ds.studify.core.data.dto.request.RequestChangeNicknameDto
import com.ds.studify.core.data.dto.request.RequestChangePasswordDto
import com.ds.studify.core.data.dto.response.BaseResponse
import kotlinx.serialization.json.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserService {

    @GET("user/profile")
    suspend fun getProfile(): BaseResponse<ProfileDto>

    @PATCH("user/change-password")
    suspend fun patchChangePassword(
        @Body body: RequestChangePasswordDto
    ): BaseResponse<JsonElement?>

    @PATCH("user/change-nickname")
    suspend fun patchChangeNickname(
        @Body body: RequestChangeNicknameDto
    ): BaseResponse<ProfileDto>
}