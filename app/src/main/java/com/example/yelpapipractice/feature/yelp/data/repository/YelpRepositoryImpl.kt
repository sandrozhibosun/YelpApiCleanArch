package com.example.yelpapipractice.feature.yelp.data.repository

import com.example.yelpapipractice.feature.yelp.data.datasource.local.YelpLocalDataSource
import com.example.yelpapipractice.feature.yelp.data.datasource.remote.YelpRemoteDataSource
import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
import com.example.yelpapipractice.feature.yelp.data.model.toDomain
import com.example.yelpapipractice.feature.yelp.data.model.toEntity
import com.example.yelpapipractice.feature.yelp.utils.IoDispatcher
import com.example.yelpapipractice.feature.yelp.utils.network.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import javax.inject.Inject

class YelpRepositoryImpl @Inject constructor(
    private val yelpRemoteDataSource: YelpRemoteDataSource,
    private val yelpLocalDataSource: YelpLocalDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : YelpRepository {

    //Cache SSOT and network refresh strategy
    override fun getBusinesses(): Flow<Resource<List<Business>>> {
        return getBusinessesCacheFirst()
    }

    private fun getBusinessesCacheFirst(): Flow<Resource<List<Business>>> {
        return yelpLocalDataSource.getBusinesses().map { it ->
            if (it.isNotEmpty()) {
                Resource.Success(it.map { it.toDomain() })
            } else {
                refreshBusinesses()
                yelpLocalDataSource.getBusinesses().first().let { businesses ->
                    if (businesses.isNotEmpty()) {
                        Resource.Success(businesses.map { it.toDomain() })
                    } else {
                        Resource.Failure(false, null, "No businesses found")
                    }
                }
            }
        }.flowOn(ioDispatcher)
    }

    override suspend fun refreshBusinesses(): Resource<Unit> {
        return withContext(ioDispatcher) {
            try {
                when (val resource = yelpRemoteDataSource.getBusinesses()) {
                    is Resource.Success -> {
                        //clean option , maybe with timestamp
                        yelpLocalDataSource.cleanAndCacheBusinesses(resource.value.businessList.map { it.toEntity() })
                        Resource.Success(Unit)
                    }

                    is Resource.Failure -> {
                        resource
                    }

                    else -> Resource.Failure(false, null, "Unknown error")
                }
            } catch (e: Exception) {
                Resource.Failure(false, null, e.message)
            }
        }
    }
}

/**  Network first cache fall back
 *
 *    override fun getCharactersNetworkFirst(): Flow<Resource<List<RMCharacter>>> {
 *         return flow {
 *             when (val resource = remoteDataSource.getCharacters()) {
 *                 is Resource.Success -> {
 *                     emit(Resource.Success(resource.value.results.map { it.toDomain() }))
 *                     saveCharactersLocally(resource.value.results.map { it.toEntity() })
 *                 }
 *
 *                 is Resource.Failure -> {
 *                     emit(resource)
 *                     emitAll(getCharactersFromLocal())
 *                 }
 *
 *                 else -> Resource.Failure(false, null, "Unknown error")
 *             }
 *         }.flowOn(ioDispatcher)
 *     }
 *
 *       private fun getCharactersFromLocal(): Flow<Resource<List<RMCharacter>>> {
 *         return localDataSource.getCharactersLocally().map { characterEntities ->
 *             if (characterEntities.isNotEmpty()) {
 *                 Resource.Success(characterEntities.map { it.toDomain() })
 *             } else {
 *                 Resource.Failure(false, null, "No data found")
 *             }
 *         }
 *     }
 *
 *     private suspend fun saveCharactersLocally(characterEntities: List<CharacterEntity>) {
 *         withContext(ioDispatcher) {
 *             localDataSource.saveCharactersLocally(characterEntities)
 *         }
 *     }
 */
