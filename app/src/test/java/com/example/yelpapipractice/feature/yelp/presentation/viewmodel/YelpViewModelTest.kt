package com.example.yelpapipractice.feature.yelp.presentation.viewmodel

import com.example.yelpapipractice.CoroutineTestRule
import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
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

class YelpViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(testDispatcher)

    private var getBusinessesUseCase: GetBusinessesUseCase = mockk()
    private var refreshBusinessUseCase: RefreshBusinessUseCase = mockk()

    private lateinit var viewModel: YelpViewModel

    // use advanced for init

    @Test
    fun `Given successful resource When view model initiate Then update UI state`() = runTest {
        //Given
        val businesses = listOf(
            YelpTestFactory.testBusiness
        )
        val businessesFlow = flowOf(Resource.Success(businesses))
        coEvery { getBusinessesUseCase() } returns businessesFlow

        //When
        viewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        //Then
        assertEquals(businesses, viewModel.uiState.value)
        assertFalse(viewModel.isRefreshing.value)
        assertFalse(viewModel.showError.value)
    }

    @Test
    fun `Given failed resource When view model initiate Then update UI state`() = runTest {
        //Given
        val businessesFlow = flowOf(Resource.Failure(false, null, "Error in view model"))
        coEvery { getBusinessesUseCase() } returns businessesFlow

        //When
        viewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        //Then
        assertEquals(emptyList<Business>(), viewModel.uiState.value)
        assertFalse(viewModel.isRefreshing.value)
        assertTrue(viewModel.showError.value)
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
            viewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
            viewModel.refreshBusinesses()
            testDispatcher.scheduler.advanceUntilIdle()


            //Then
            assertFalse(viewModel.isRefreshing.value)
            assertFalse(viewModel.showError.value)
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
        viewModel = YelpViewModel(getBusinessesUseCase, refreshBusinessUseCase)
        viewModel.refreshBusinesses()
        testDispatcher.scheduler.advanceUntilIdle()

        //Then
        assertFalse(viewModel.isRefreshing.value)
        assertTrue(viewModel.showError.value)
    }
}