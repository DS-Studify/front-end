package com.ds.studify.core.data.di

import com.ds.studify.core.data.repository.AuthRepository
import com.ds.studify.core.data.repository.StudyRecordRepository
import com.ds.studify.core.data.repository.StudyRepository
import com.ds.studify.core.data.repository.TokenRepository
import com.ds.studify.core.data.repository.UserRepository
import com.ds.studify.core.data.repository_impl.AuthRepositoryImpl
import com.ds.studify.core.data.repository_impl.StudyRecordRepositoryImpl
import com.ds.studify.core.data.repository_impl.StudyRepositoryImpl
import com.ds.studify.core.data.repository_impl.TokenRepositoryImpl
import com.ds.studify.core.data.repository_impl.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindAuthRepository(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    fun bindTokenRepository(
        repositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    fun studyRecordRepository(
        repositoryImpl: StudyRecordRepositoryImpl
    ): StudyRecordRepository

    @Binds
    fun bindStudyRepository(
        repositoryImpl: StudyRepositoryImpl
    ): StudyRepository

    @Binds
    fun bindUserRepository(
        repositoryImpl: UserRepositoryImpl
    ): UserRepository

}