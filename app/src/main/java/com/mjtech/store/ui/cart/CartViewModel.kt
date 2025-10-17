package com.mjtech.store.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Product
import com.mjtech.store.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private val _cartUiState = MutableStateFlow(CartUiState())
    val cartUiState: StateFlow<CartUiState> = _cartUiState

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
}