package com.example.yelpapipractice.feature.yelp.utils.network

import retrofit2.Response

/**
 * Sealed class to encapsulate the different states of data.
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()

    data class Success<out T>(val value: T) : Resource<T>()

    data class Failure(
        val isNetworkException: Boolean,
        val errorCode: Int? = null,
        val errorDescription: String? = null
    ) : Resource<Nothing>()
}

fun <T> Response<T>.toResource(): Resource<T> {
    return try {
        if (this.isSuccessful && this.body() != null) {
            Resource.Success(
                this.body()!!
            )
        } else {
            Resource.Failure(
                false,
                this.code(),
                this.message()
            )
        }
    } catch (throwable: Throwable) {
        Resource.Failure(
            true,
            null,
            throwable.message
        )
    }
}