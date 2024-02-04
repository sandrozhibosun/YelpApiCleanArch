package com.example.yelpapipractice.feature.yelp.domain.usecase

import com.example.yelpapipractice.feature.yelp.data.repository.YelpRepository
import javax.inject.Inject

class RefreshBusinessUseCase @Inject constructor(
    private val yelpRepository: YelpRepository
) {
    suspend operator fun invoke() = yelpRepository.refreshBusinesses()
}