package com.example.ecommerce.presentation.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.usecase.products.GetProductsUseCase
import com.example.ecommerce.presentation.filter.FilterOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GetProductsUseCase
): ViewModel() {

    private val _products = MutableStateFlow<PagingData<Product>>(PagingData.empty())
    val products: StateFlow<PagingData<Product>> = _products

    private val _filterOptions = MutableStateFlow<FilterOptions?>(null)
    val filterOptions: StateFlow<FilterOptions?> = _filterOptions

    fun setFilterOptions(options: FilterOptions) {
        _filterOptions.value = options
        getProducts(options)
    }

    fun getProducts(filterOptions: FilterOptions? = null) {
        viewModelScope.launch {
            useCase.executeGetProducts(filterOptions ?: FilterOptions()).cachedIn(viewModelScope).collectLatest { pagingData ->
                _products.value = pagingData
            }
        }
    }
}