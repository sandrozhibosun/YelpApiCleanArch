package com.example.yelpapipractice.core.di

import android.content.Context
import androidx.room.Room
import com.example.yelpapipractice.core.data.database.YelpDatabase
import com.example.yelpapipractice.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        YelpDatabase::class.java,
        Constants.YELP_DATABASE_NAME
    )
        .build()
}