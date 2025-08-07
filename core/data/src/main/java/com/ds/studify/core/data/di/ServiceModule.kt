package com.ds.studify.core.data.di

import com.ds.studify.core.data.di.qualifier.NoToken
import com.ds.studify.core.data.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(@NoToken retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

}