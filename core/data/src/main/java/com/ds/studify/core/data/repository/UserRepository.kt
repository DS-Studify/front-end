package com.ds.studify.core.data.repository

import com.ds.studify.core.domain.entity.ProfileEntity

interface UserRepository {
    suspend fun getProfile(): Result<ProfileEntity>
    suspend fun patchChangePassword(originPassword: String, newPassword: String): Result<Unit>
    suspend fun patchChangeNickname(nickname: String): Result<ProfileEntity>
}