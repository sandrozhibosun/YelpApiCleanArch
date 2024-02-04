package com.example.yelpapipractice.feature.yelp.data.datasource.remote

import android.util.Log
import androidx.paging.LOGGER
import com.example.yelpapipractice.feature.yelp.utils.network.toResource
import javax.inject.Inject

class YelpRemoteDataSource @Inject constructor(
    private val yelpService: YelpService
) {
    suspend fun getBusinesses() = yelpService.getBusinesses().toResource()
}