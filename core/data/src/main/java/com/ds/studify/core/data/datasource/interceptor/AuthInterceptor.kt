package com.ds.studify.core.data.datasource.interceptor

import android.content.Intent
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.ds.studify.core.data.datasource.AuthDataSource
import com.ds.studify.core.data.datasource.TokenDataSource
import com.ds.studify.core.data.dto.request.RequestReissueToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val tokenDataSource: TokenDataSource,
    private val authDataSource: AuthDataSource,
    @ApplicationContext private val context: Context
) : Interceptor {

    private val mutex = Mutex()

    private fun Request.addAuthorizationHeader() =
        this.newBuilder().addHeader(AUTHORIZATION, "$BEARER ${tokenDataSource.accessToken}").build()

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val authRequest =
            if (tokenDataSource.accessToken.isNotBlank()) originalRequest.addAuthorizationHeader() else originalRequest
        var response = chain.proceed(authRequest)

        when (response.code) {
            TOKEN_EXPIRED -> {
                response.close()
                response = handleTokenExpiration(
                    chain = chain,
                    originalRequest = originalRequest,
                    requestAccessToken = tokenDataSource.accessToken
                )
            }
        }

        return response
    }

    private fun handleTokenExpiration(
        chain: Interceptor.Chain,
        originalRequest: Request,
        requestAccessToken: String
    ): Response = runBlocking {
        mutex.withLock {
            when (isTokenValid(
                requestAccessToken = requestAccessToken,
                currentAccessToken = tokenDataSource.accessToken
            )) {
                true -> chain.proceed(originalRequest.addAuthorizationHeader())
                false -> handleTokenRefresh(
                    chain = chain,
                    originalRequest = originalRequest,
                    refreshToken = tokenDataSource.refreshToken
                )
            }
        }
    }

    private fun isTokenValid(requestAccessToken: String, currentAccessToken: String): Boolean =
        requestAccessToken != currentAccessToken && currentAccessToken.isNotBlank()

    private fun handleTokenRefresh(
        chain: Interceptor.Chain,
        originalRequest: Request,
        refreshToken: String
    ): Response {
        val result = runCatching {
            runBlocking {
                authDataSource.postTokenReissue(RequestReissueToken(refreshToken))
            }
        }

        return result.fold(
            onSuccess = { response ->
                val newAccessToken = response.data.accessToken

                if (newAccessToken.isBlank()) {
                    return handleTokenRefreshFailed()
                }

                with(tokenDataSource) {
                    accessToken = newAccessToken
                }

                Log.d("reissueToken", newAccessToken)

                chain.proceed(originalRequest.addAuthorizationHeader())
            },
            onFailure = { error ->
                Log.e("reissueToken", "Token reissue failed: $error")
                handleTokenRefreshFailed()
            }
        )
    }

    private fun handleTokenRefreshFailed(): Response {
        Handler(Looper.getMainLooper()).post {
            restartApp()
        }

        tokenDataSource.clearToken()

        throw IllegalStateException("Token reissue failed. Restarting app.")
    }

    private fun restartApp() {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            Runtime.getRuntime().exit(0)
        } else {
            throw IllegalStateException("Cannot restart app: Launch intent not found")
        }
    }

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
        private const val TOKEN_EXPIRED = 401
    }
}