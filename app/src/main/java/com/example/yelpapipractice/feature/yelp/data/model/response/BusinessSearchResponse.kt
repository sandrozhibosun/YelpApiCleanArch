package com.example.yelpapipractice.feature.yelp.data.model.response

import com.google.gson.annotations.SerializedName

data class BusinessSearchResponse(
    @SerializedName("businesses") val businessList: List<BusinessResponse>
)

data class BusinessResponse (
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val url : String,
    @SerializedName("is_closed") val isClosed : Boolean,
    @SerializedName("ratings") val ratings : Float,
)
