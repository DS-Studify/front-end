package com.ds.studify.core.data.di

import com.ds.studify.core.data.datasource.AuthDataSource
import com.ds.studify.core.data.service.AuthService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataSourceModule {

    @Provides
    @Singleton
    fun providesAuthDataSource(
        authService: AuthService
    ): AuthDataSource = AuthDataSource(authService)

}