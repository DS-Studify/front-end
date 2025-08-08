package com.ds.studify.core.data.di

import com.ds.studify.core.data.di.qualifier.JWT
import com.ds.studify.core.data.di.qualifier.NoToken
import com.ds.studify.core.data.service.AuthService
import com.ds.studify.core.data.service.StudyRecordService
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

    @Provides
    @Singleton
    fun provideStudyRecordService(@JWT retrofit: Retrofit): StudyRecordService =
        retrofit.create(StudyRecordService::class.java)

}