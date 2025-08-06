package com.ds.studify.core.data.datasource.interceptor

import com.ds.studify.core.data.datasource.TokenDataSource
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : Interceptor {

    private fun Request.addAuthorizationHeader() =
        this.newBuilder().addHeader(AUTHORIZATION, "$BEARER ${tokenDataSource.accessToken}").build()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest =
            if (tokenDataSource.accessToken.isNotBlank()) originalRequest.addAuthorizationHeader() else originalRequest
        val response = chain.proceed(authRequest)

        return response
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
    }
}