package com.example.yelpapipractice.feature.yelp.presentation.viewmodel

import com.example.yelpapipractice.CoroutineTestRule
import com.example.yelpapipractice.feature.yelp.domain.usecase.GetBusinessesUseCase
import com.example.yelpapipractice.feature.yelp.domain.usecase.RefreshBusinessUseCase
import com.example.yelpapipractice.feature.yelp.utils.network.Resource
import com.example.yelpapipractice.utils.YelpTestFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class YelpViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher)

    private var getBusinessesUseCase: GetBusinessesUseCase = mockk()
    private var refreshBusinessUseCase: RefreshBusinessUseCase = mockk()

    private lateinit var yelpViewModel: YelpViewModel


    @Test
    fun `Given successful resource When view model initiate Then update UI state`() = runTest {
        //Given
        val businesses = listOf(
            YelpTestFactory.testBusiness
        )
        val businessesFlow = flowOf(Resource.Success(businesses))
        coEvery { getBusinessesUseCase() } returns businessesFlow

        //When
        yelpViewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
        testDispatcher.scheduler.advanceUntilIdle()


        //Then
        assertTrue(yelpViewModel.uiState.value is Resource.Success)
        assertEquals(businesses, (yelpViewModel.uiState.value as Resource.Success).value)
    }

    @Test
    fun `Given failed resource When view model initiate Then update UI state`() = runTest {
        //Given

        val businessesFlow = flowOf(Resource.Failure(false, null, "Error in view model"))
        coEvery { getBusinessesUseCase() } returns businessesFlow

        //When
        yelpViewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
        testDispatcher.scheduler.advanceUntilIdle()


        //Then
        assertTrue(yelpViewModel.uiState.value is Resource.Failure)
    }

    @Test
    fun `Given success resource When view model refreshBusinesses Then update UI state`() =
        runTest {
            //Given
            val businesses = listOf(
                YelpTestFactory.testBusiness
            )
            val businessesFlow = flowOf(Resource.Success(businesses))
            coEvery { getBusinessesUseCase() } returns businessesFlow
            coEvery { refreshBusinessUseCase() } returns Resource.Success(Unit)

            //When
            yelpViewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
            yelpViewModel.refreshBusinesses()
            testDispatcher.scheduler.advanceUntilIdle()


            //Then
            assertTrue(yelpViewModel.uiState.value is Resource.Success)
        }

    @Test
    fun `Given failed resource When view model refreshBusinesses Then update UI state`() = runTest {
        //Given

        val businessesFlow = flowOf(Resource.Failure(false, null, "Error in view model"))
        coEvery { getBusinessesUseCase() } returns businessesFlow
        coEvery { refreshBusinessUseCase() } returns Resource.Failure(
            false,
            null,
            "Error in view model"
        )

        //When
        yelpViewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
        yelpViewModel.refreshBusinesses()
        testDispatcher.scheduler.advanceUntilIdle()


        //Then
        assertTrue(yelpViewModel.uiState.value is Resource.Failure)
    }

}