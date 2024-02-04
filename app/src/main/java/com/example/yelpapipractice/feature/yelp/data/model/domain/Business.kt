package com.example.yelpapipractice.feature.yelp.data.model.domain

data class Business(
    val name: String,
    val imageUrl: String,
    val isClosed: Boolean,
    val ratings: Float
)