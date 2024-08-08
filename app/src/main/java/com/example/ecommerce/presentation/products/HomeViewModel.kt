package com.example.ecommerce.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.usecase.products.GetProductsUseCase
import com.example.ecommerce.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetProductsUseCase
): ViewModel() {

    private val _products = MutableStateFlow<ProductState?>(null)
    val products: StateFlow<ProductState?> = _products

     fun getProducts() {
        useCase.executeGetProducts().onEach { result ->
            when (result) {
                is Result.Success -> {
                    _products.value = ProductState(products = result.data)
                }

                is Result.Error -> {
                    _products.value = ProductState(error = result.message)
                }

                is Result.Loading -> {
                    _products.value = ProductState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}