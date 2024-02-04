package com.example.yelpapipractice.feature.yelp.data.repository

import com.example.yelpapipractice.CoroutineTestRule
import com.example.yelpapipractice.feature.yelp.data.datasource.local.YelpLocalDataSource
import com.example.yelpapipractice.feature.yelp.data.datasource.remote.YelpRemoteDataSource
import com.example.yelpapipractice.feature.yelp.utils.network.Resource
import com.example.yelpapipractice.utils.YelpTestFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class YelpRepositoryImplTest {

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher)

    private lateinit var repository: YelpRepositoryImpl

    private val localDataSource: YelpLocalDataSource = mockk()
    private val remoteDataSource: YelpRemoteDataSource = mockk()

    @Before
    fun setup() {
        repository = YelpRepositoryImpl(remoteDataSource, localDataSource, testDispatcher)
    }

    @Test
    fun `getBusinesses returns cached businesses successfully`() = runTest {
        // Given
        val businesses = listOf(
            YelpTestFactory.testBusiness
        )
        coEvery { localDataSource.getBusinesses() } returns flowOf(listOf(YelpTestFactory.testBusinessEntity))

        // When
        val result = repository.getBusinesses().first()

        // Then
        coVerify(exactly = 0) { remoteDataSource.getBusinesses() }
        assertTrue(result is Resource.Success)
        assertEquals(businesses, (result as Resource.Success).value)
    }

    @Test
    fun `getBusinesses when cache is empty then fetch data successfully from remote`() = runTest {
        // Given
        val businesses = listOf(
            YelpTestFactory.testBusiness
        )
        coEvery { localDataSource.getBusinesses() } returns flowOf(emptyList())
        coEvery { remoteDataSource.getBusinesses() } returns Resource.Success(YelpTestFactory.testBusinessSearchResponse)
        coEvery { localDataSource.cleanAndCacheBusinesses(any()) } returns Unit

        // When
        val result = repository.getBusinesses().first()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { remoteDataSource.getBusinesses() }
        coVerify(exactly = 1) { localDataSource.cleanAndCacheBusinesses(listOf(YelpTestFactory.testBusinessEntity)) }
    }

    @Test
    fun `getBusinesses when cache is empty then fetch data failed from remote`() = runTest {
        // Given
        val businesses = listOf(
            YelpTestFactory.testBusiness
        )
        coEvery { localDataSource.getBusinesses() } returns flowOf(emptyList())
        coEvery { remoteDataSource.getBusinesses() } returns Resource.Failure(
            false,
            null,
            "Unknown error"
        )

        // When
        val result = repository.getBusinesses().first()

        // Then
        coVerify(exactly = 1) { remoteDataSource.getBusinesses() }
        assertTrue(result is Resource.Failure)
    }

    @Test
    fun `refreshBusinesses when fetch data successfully from remote then update cache`() = runTest {
        // Given

        coEvery { remoteDataSource.getBusinesses() } returns Resource.Success(YelpTestFactory.testBusinessSearchResponse)
        coEvery { localDataSource.cleanAndCacheBusinesses(any()) } returns Unit

        // When
        val result = repository.refreshBusinesses()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { localDataSource.cleanAndCacheBusinesses(any()) }
    }

    @Test
    fun `refreshBusinesses when fetch data failed from remote then not update cache`() = runTest {
        // Given

        coEvery { remoteDataSource.getBusinesses() } returns Resource.Failure(
            false,
            null,
            "Unknown error"
        )

        // When
        val result = repository.refreshBusinesses()

        // Then
        coVerify(exactly = 0) { localDataSource.cleanAndCacheBusinesses(any()) }
    }

}