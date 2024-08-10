package com.example.ecommerce.presentation.cart

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.data.model.Product
import com.example.ecommerce.domain.usecase.cart.ClearCartUseCase
import com.example.ecommerce.domain.usecase.cart.DeleteCartUseCase
import com.example.ecommerce.domain.usecase.cart.GetCartsUseCase
import com.example.ecommerce.domain.usecase.cart.SaveCartUseCase
import com.example.ecommerce.domain.usecase.cart.UpdateProductQuantityUseCase
import com.example.ecommerce.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val saveCartUseCase: SaveCartUseCase,
    private val getCartsUseCase: GetCartsUseCase,
    private val deleteCartUseCase: DeleteCartUseCase,
    private val updateProductQuantityUseCase: UpdateProductQuantityUseCase,
    private val clearCartUseCase: ClearCartUseCase,
) : ViewModel() {

    private val _cartState = MutableStateFlow<CartState?>(null)
    val cartState: StateFlow<CartState?> = _cartState

    private val _totalPrice = MutableStateFlow("0.00")
    val totalPrice: StateFlow<String> = _totalPrice

    fun updateQuantity(productId: Int, quantity: Int) {
        Log.d("CartViewModel", "Before Update - Product ID: $productId, Quantity: $quantity")
        viewModelScope.launch {
            updateProductQuantityUseCase(productId, quantity).onEach{ result ->
                when (result) {
                    is Result.Success -> {
                        getCartList()
                        Log.d("CartViewModel", "Update Success - Product ID: $productId, Quantity: $quantity")
                    }

                    is Result.Error -> {
                        _cartState.value = CartState(error = result.message)
                    }
                    is Result.Loading -> {
                        _cartState.value = CartState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
        fun saveCart(product: Product) {
            viewModelScope.launch {
                saveCartUseCase(product).onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            getCartList()
                        }

                        is Result.Error -> {
                            _cartState.value = CartState(error = result.message)
                        }

                        is Result.Loading -> {
                            _cartState.value = CartState(isLoading = true)
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }

        fun getCartList() {
            viewModelScope.launch {
                getCartsUseCase().onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            Log.d("CartViewModel", "Cart List Retrieved: ${result.data}")
                            _cartState.value = CartState(products = result.data)
                            _totalPrice.value = calculateTotalPrice(result.data)
                        }

                        is Result.Error -> {
                            _cartState.value = CartState(error = result.message)
                        }

                        is Result.Loading -> {
                            _cartState.value = CartState(isLoading = true)
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }
        fun deleteCart(product: Product) {
            viewModelScope.launch {
                deleteCartUseCase(product).onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            getCartList()
                        }

                        is Result.Error -> {
                            _cartState.value = CartState(error = result.message)
                        }

                        is Result.Loading -> {
                            _cartState.value = CartState(isLoading = true)
                        }
                    }
                }.launchIn(viewModelScope)
            }
        }

        @SuppressLint("DefaultLocale")
        private fun calculateTotalPrice(products: List<Product>): String {
            val total = products.sumOf { it.price?.toDoubleOrNull()?.times(it.quantity!!) ?: 0.0 }
            return String.format("%.2f", total)
        }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase().onEach { result ->
                when (result) {
                    is Result.Success -> {
                        _cartState.value = CartState(products = emptyList())
                        _totalPrice.value = "0.00"
                    }

                    is Result.Error -> {
                        _cartState.value = CartState(error = result.message)
                    }

                    is Result.Loading -> {
                        _cartState.value = CartState(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    }