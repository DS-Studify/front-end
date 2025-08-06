package com.ds.studify.core.data.di

import com.ds.studify.core.data.repository.StatsRepository
import com.ds.studify.core.data.repository.TokenRepository
import com.ds.studify.core.data.repository_impl.StatsRepositoryImpl
import com.ds.studify.core.data.repository_impl.TokenRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindTokenRepository(
        repositoryImpl: TokenRepositoryImpl
    ): TokenRepository

    @Binds
    fun bindStatsRepository(
        repositoryImpl: StatsRepositoryImpl
    ): StatsRepository

}