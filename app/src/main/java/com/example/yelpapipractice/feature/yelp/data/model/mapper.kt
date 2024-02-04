package com.example.yelpapipractice.feature.yelp.data.model

import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
import com.example.yelpapipractice.feature.yelp.data.model.entity.BusinessEntity
import com.example.yelpapipractice.feature.yelp.data.model.response.BusinessResponse

fun BusinessResponse.toEntity(): BusinessEntity {
    return BusinessEntity(
        name = this.name,
        imageUrl = this.url,
        isClosed = this.isClosed,
        ratings = this.ratings
    )
}

fun BusinessEntity.toDomain(): Business {
    return Business(
        name = this.name,
        imageUrl = this.imageUrl,
        isClosed = this.isClosed,
        ratings = this.ratings
    )
}

fun BusinessResponse.toDomain(): Business {
    return Business(
        name = this.name,
        imageUrl = this.url,
        isClosed = this.isClosed,
        ratings = this.ratings
    )
}