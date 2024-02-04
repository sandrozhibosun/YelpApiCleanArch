package com.example.yelpapipractice.core.di

import com.example.yelpapipractice.feature.yelp.data.repository.YelpRepository
import com.example.yelpapipractice.feature.yelp.data.repository.YelpRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindYelpRepository(
        yelpRepositoryImpl: YelpRepositoryImpl
    ): YelpRepository
}