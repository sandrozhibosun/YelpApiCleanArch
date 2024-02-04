package com.example.yelpapipractice.feature.yelp.presentation.viewmodel

import android.util.Log
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
) : ViewModel() {

    private val _uiState = MutableStateFlow<List<Business>>(emptyList())
    val uiState = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError = _showError.asStateFlow()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _showError.value = true
    }

    init {
        getBusinesses()
    }

    private fun getBusinesses() {
        getBusinessesUseCase()
            .onStart { _isRefreshing.value = true }
            .catch { emit(Resource.Failure(false, null, "Error in view model")) }
            .onEach {
                when (it) {
                    is Resource.Success -> {
                        _isRefreshing.value = false
                        _uiState.value = it.value
                    }

                    is Resource.Failure -> {
                        _isRefreshing.value = false
                        _showError.value = true
                    }

                    else -> {
                        _isRefreshing.value = true
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun refreshBusinesses() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _isRefreshing.value = true
            when (val resource = refreshBusinessUseCase()) {
                is Resource.Success -> {
                    _isRefreshing.value = false
                }

                is Resource.Failure -> {
                    _isRefreshing.value = false
                    _showError.value = true
                }

                else -> {
                    _isRefreshing.value = true
                }
            }
        }
    }
}