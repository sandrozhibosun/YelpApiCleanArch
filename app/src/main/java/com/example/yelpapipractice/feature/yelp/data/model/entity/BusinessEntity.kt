package com.example.yelpapipractice.feature.yelp.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "businesses")
data class BusinessEntity(
    val name: String,
    val imageUrl: String,
    val isClosed: Boolean,
    val ratings: Float
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}