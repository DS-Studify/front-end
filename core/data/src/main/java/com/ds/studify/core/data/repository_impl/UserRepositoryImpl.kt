package com.ds.studify.core.data.repository_impl

import com.ds.studify.core.data.datasource.UserDataSource
import com.ds.studify.core.data.dto.request.RequestChangeNicknameDto
import com.ds.studify.core.data.dto.request.RequestChangePasswordDto
import com.ds.studify.core.data.repository.UserRepository
import com.ds.studify.core.domain.entity.ProfileEntity
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val dataSource: UserDataSource
) : UserRepository {

    override suspend fun getProfile(): Result<ProfileEntity> = runCatching {
        val res = dataSource.getProfile()
        if (res.status.toInt() == 200 || res.code == "SUCCESS_PROFILE") {
            val dto = res.data
            ProfileEntity(email = dto.email, nickname = dto.nickname)
        } else {
            error(res.message)
        }
    }

    override suspend fun patchChangePassword(originPassword: String, newPassword: String): Result<Unit> =
        runCatching {
            val res = dataSource.patchChangePassword(
                RequestChangePasswordDto(originPassword, newPassword)
            )
            when {
                res.status.toInt() == 200 || res.code == "SUCCESS_CHANGE_PASSWORD" -> Unit
                res.status.toInt() == 403 || res.code == "INCORRECT_PASSWORD" -> error("INCORRECT_PASSWORD")
                else -> error(res.message)
            }
        }

    override suspend fun patchChangeNickname(nickname: String): Result<ProfileEntity> = runCatching {
        return try {
            val res = dataSource.patchChangeNickname(RequestChangeNicknameDto(nickname))
            if (res.status.toInt() == 200 || res.code == "SUCCESS_CHANGE_NICKNAME") {
                val dto = res.data
                Result.success(ProfileEntity(email = dto.email, nickname = dto.nickname))
            } else {
                Result.failure(IllegalStateException(res.message))
            }
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 403) {
                Result.failure(IllegalStateException("NICKNAME_FORBIDDEN"))
            } else {
                Result.failure(e)
            }
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}