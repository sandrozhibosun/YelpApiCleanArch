package com.example.yelpapipractice.feature.yelp.domain.usecase

import com.example.yelpapipractice.feature.yelp.data.repository.YelpRepository
import javax.inject.Inject

class GetBusinessesUseCase @Inject constructor(
    private val yelpRepository: YelpRepository
) {
    operator fun invoke() = yelpRepository.getBusinesses()
}
