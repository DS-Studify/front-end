package com.ds.studify.core.data.datasource

import com.ds.studify.core.data.dto.request.ProfileDto
import com.ds.studify.core.data.dto.request.RequestChangeNicknameDto
import com.ds.studify.core.data.dto.request.RequestChangePasswordDto
import com.ds.studify.core.data.dto.response.BaseResponse
import com.ds.studify.core.data.service.UserService
import kotlinx.serialization.json.JsonElement
import javax.inject.Inject

class  UserDataSource @Inject constructor(
    private val service: UserService
) {
    suspend fun getProfile(): BaseResponse<ProfileDto> =
        service.getProfile()

    suspend fun patchChangePassword(body: RequestChangePasswordDto): BaseResponse<JsonElement?> =
        service.patchChangePassword(body)

    suspend fun patchChangeNickname(body: RequestChangeNicknameDto): BaseResponse<ProfileDto> =
        service.patchChangeNickname(body)

    suspend fun deleteUser(): BaseResponse<String> =
        service.deleteUser()
}