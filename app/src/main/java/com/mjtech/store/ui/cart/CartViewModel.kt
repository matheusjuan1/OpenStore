package com.mjtech.store.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Product
import com.mjtech.store.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState

    init {
        getCartMetrics()
    }

    fun onAddProductToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addItem(product).collect { result ->
                _cartUiState.update { currentState ->
                    currentState.copy(addItemState = result)
                }
            }
        }
    }

    fun onRemoveProductFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.removeItem(product).collect { result ->
                _cartUiState.update { currentState ->
                    currentState.copy(removeItemState = result)
                }
            }
        }
    }

    fun resetAddItemState() {
        _cartUiState.update { currentState ->
            currentState.copy(addItemState = DataResult.Success(Unit))
        }
    }

    fun resetRemoveItemState() {
        _cartUiState.update { currentState ->
            currentState.copy(removeItemState = DataResult.Success(Unit))
        }
    }

    private fun getCartMetrics() {
        // Total Price
        val totalPriceFlow = cartRepository.getTotalPrice()
            .map { result ->
                if (result is DataResult.Success) result.data else 0.0
            }
            .catch { emit(0.0) }

        // Total items count
        val itemsMapFlow = cartRepository.getCartItems()
            .map { result ->
                if (result is DataResult.Success) {
                    result.data.associate { it.product.id to it.quantity }
                } else {
                    emptyMap()
                }
            }
            .catch { emit(emptyMap()) }

        // Combine flows
        totalPriceFlow.combine(itemsMapFlow) { price, quantitiesMap ->
            val totalItemsCount = quantitiesMap.values.sum()

            // Quantity per product
            val quantitiesProducts = quantitiesMap.mapKeys { it.key }

            _cartUiState.update { currentState ->
                currentState.copy(
                    totalPrice = price,
                    totalItemsCount = totalItemsCount,
                    quantitiesProducts = quantitiesProducts
                )
            }
        }.launchIn(viewModelScope)
    }
}