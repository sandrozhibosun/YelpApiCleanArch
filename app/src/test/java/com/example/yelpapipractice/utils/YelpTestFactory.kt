package com.example.yelpapipractice.utils

import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
import com.example.yelpapipractice.feature.yelp.data.model.entity.BusinessEntity
import com.example.yelpapipractice.feature.yelp.data.model.response.BusinessResponse
import com.example.yelpapipractice.feature.yelp.data.model.response.BusinessSearchResponse

object YelpTestFactory {

    val testBusinessEntity = BusinessEntity(
        name = "Test Business",
        imageUrl = "https://example.com/image.jpg",
        isClosed = false,
        ratings = 4.5f
    ).apply {
        id = 1 // Assuming you want to set the ID manually for the test
    }

    val testBusiness = Business(
        name = "Test Business",
        imageUrl = "https://example.com/image.jpg",
        isClosed = false,
        ratings = 4.5f
    )

    val testBusinessResponse = BusinessResponse(
        name = "Test Business",
        url = "https://example.com/image.jpg",
        isClosed = false,
        ratings = 4.5f
    )

    val testBusinessSearchResponse = BusinessSearchResponse(
        businessList = listOf(testBusinessResponse) // You can add more instances to the list if needed
    )
}