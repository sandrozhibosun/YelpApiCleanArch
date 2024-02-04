package com.example.yelpapipractice.feature.yelp.data.datasource.remote

import com.example.yelpapipractice.feature.yelp.data.model.response.BusinessSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface YelpService {

    @GET("businesses/search")
    suspend fun getBusinesses(
        @Query("location") location: String = "NYC",
        @Query("sort_by") sortBy: String = "best_match",
        @Query("limit") limit: Int = 10,
    ): Response<BusinessSearchResponse>
}