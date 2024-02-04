package com.example.yelpapipractice.feature.yelp.data.datasource.local

import com.example.yelpapipractice.core.data.database.YelpDatabase
import com.example.yelpapipractice.feature.yelp.data.model.entity.BusinessEntity
import javax.inject.Inject

class YelpLocalDataSource @Inject constructor(
    private val yelpDatabase: YelpDatabase
) {
    fun getBusinesses() = yelpDatabase.getBusinessDao().getBusinesses()
    suspend fun cleanAndCacheBusinesses(businesses: List<BusinessEntity>) = yelpDatabase.getBusinessDao().cleanAndCacheBusinesses(businesses)
}