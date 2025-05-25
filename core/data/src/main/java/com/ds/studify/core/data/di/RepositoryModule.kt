package com.ds.studify.core.data.di

import com.ds.studify.core.data.repository.StatsRepository
import com.ds.studify.core.data.repository_impl.StatsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {

    @Binds
    fun bindStatsRepository(
        repositoryImpl: StatsRepositoryImpl
    ): StatsRepository

}