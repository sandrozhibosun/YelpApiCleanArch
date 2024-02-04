package com.example.yelpapipractice.feature.yelp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yelpapipractice.feature.yelp.data.model.domain.Business
import com.example.yelpapipractice.feature.yelp.domain.usecase.GetBusinessesUseCase
import com.example.yelpapipractice.feature.yelp.domain.usecase.RefreshBusinessUseCase
import com.example.yelpapipractice.feature.yelp.utils.network.Resource
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YelpViewModel @Inject constructor(
    private val getBusinessesUseCase: GetBusinessesUseCase,
    private val refreshBusinessUseCase: RefreshBusinessUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<Resource<List<Business>>>( Resource.Loading)
    val uiState = _uiState.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _uiState.value = Resource.Failure(false, null, "Error in view model ${exception.message}")
    }

    init {
        getBusinessesUseCase()
            .onStart { emit(Resource.Loading) }
            .catch { emit(Resource.Failure(false, null, "Error in view model")) }
            .onEach {
                _uiState.value = it
            }
            .launchIn(viewModelScope)
    }

    fun refreshBusinesses() {
        viewModelScope.launch(coroutineExceptionHandler) {
            when (val resource = refreshBusinessUseCase()) {
                is Resource.Success -> {
                    getBusinessesUseCase()
                }

                is Resource.Failure -> {
                    _uiState.value = resource
                }

                else -> {

                }
            }
        }
    }
}