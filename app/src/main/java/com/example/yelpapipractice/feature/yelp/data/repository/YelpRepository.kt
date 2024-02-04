package com.example.yelpapipractice.feature.yelp.data.repository

import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
import com.example.yelpapipractice.feature.yelp.utils.network.Resource
import kotlinx.coroutines.flow.Flow

interface YelpRepository {

    //Cache as SSOT, network call as fallback
    fun getBusinesses(): Flow<Resource<List<Business>>>
    suspend fun refreshBusinesses(): Resource<Unit>
}