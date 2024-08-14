package com.example.ecommerce.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.usecase.filter.GetFilterUseCase
import com.example.ecommerce.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getFilterUseCase: GetFilterUseCase
) : ViewModel() {

    private val _filterState = MutableStateFlow<Result<List<Product>>>(Result.Loading)
    val filterState: StateFlow<Result<List<Product>>> = _filterState

    init {
        fetchFilters()
    }

    fun fetchFilters(brand: String? = null, model: String? = null) {
        viewModelScope.launch {
            getFilterUseCase(brand = brand, model = model).collect { result ->
                _filterState.value = when(result){
                    is Result.Loading -> Result.Loading
                    is Result.Success -> Result.Success(result.data)
                    is Result.Error -> Result.Error(result.message)
                }
            }
        }
    }

    fun getBrands(products: List<Product>): List<String?> {
        return products.map { it.brand }.distinct()
    }

    fun getModels(products: List<Product>): List<String?> {
        return products.map { it.model }.distinct()
    }
}