package com.ds.studify.core.data.di

import com.ds.studify.core.data.di.qualifier.JWT
import com.ds.studify.core.data.repository.UserRepository
import com.ds.studify.core.data.repository_impl.UserRepositoryImpl
import com.ds.studify.core.data.service.UserService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UserBindModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(impl: UserRepositoryImpl): UserRepository
}

@Module
@InstallIn(SingletonComponent::class)
object UserProvideModule {
    @Provides
    @Singleton
    fun provideUserService(
        @JWT retrofit: Retrofit
    ): UserService =
        retrofit.create(UserService::class.java)
}