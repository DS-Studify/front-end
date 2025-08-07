package com.ds.studify.core.data.datasource

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class TokenDataSource @Inject constructor(
    private val tokenDataSource: SharedPreferences
) {
    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }

    var accessToken: String
        get() = tokenDataSource.getString(ACCESS_TOKEN, "") ?: ""
        set(value) = tokenDataSource.edit { putString(ACCESS_TOKEN, value) }

    var refreshToken: String
        get() = tokenDataSource.getString(REFRESH_TOKEN, "") ?: ""
        set(value) = tokenDataSource.edit { putString(REFRESH_TOKEN, value) }

    fun clearToken() {
        tokenDataSource.edit().clear().apply()
    }
}