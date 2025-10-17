package com.mjtech.store.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Product
import com.mjtech.store.domain.repository.CartRepository
import com.mjtech.store.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState

    private val allProductsFlow: StateFlow<DataResult<List<Product>>>
        get() = productsRepository.getProducts()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = DataResult.Loading
            )

    init {
        getCategories()
        viewModelScope.launch {
            getFilteredProductsFlow(allProductsFlow, _uiState)
                .collectLatest { filteredListResult ->
                    _uiState.update { currentState ->
                        currentState.copy(products = filteredListResult)
                    }
                }
        }
    }

    fun onCategorySelected(categoryId: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedCategoryId = categoryId)
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
    }

    fun onAddProductToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addItem(product).collect { result ->
                _uiState.update { currentState ->
                    currentState.copy(addItemState = result)
                }
            }
        }
    }

    fun onRemoveProductFromCart(product: Product) {
        viewModelScope.launch {
            cartRepository.removeItem(product).collect { result ->
                _uiState.update { currentState ->
                    currentState.copy(removeItemState = result)
                }
            }
        }
    }

    fun resetAddItemState() {
        _uiState.update { currentState ->
            currentState.copy(addItemState = DataResult.Success(Unit))
        }
    }

    fun resetRemoveItemState() {
        _uiState.update { currentState ->
            currentState.copy(removeItemState = DataResult.Success(Unit))
        }
    }

    private fun getCategories() {
        viewModelScope.launch {
            productsRepository.getCategories().collect { result ->
                _uiState.update { currentState ->
                    currentState.copy(categories = result)
                }
            }
        }
    }

    private fun getFilteredProductsFlow(
        allProductsFlow: Flow<DataResult<List<Product>>>,
        uiState: StateFlow<ProductsUiState>
    ): StateFlow<DataResult<List<Product>>> {
        // Filtra os produtos com base nos filtros
        return allProductsFlow.combine(uiState) { productsResult, uiState ->
            when (productsResult) {
                is DataResult.Success -> {
                    val selectedCategoryId = uiState.selectedCategoryId
                    val searchQuery = uiState.searchQuery

                    var filteredList = productsResult.data

                    if (selectedCategoryId != "0") {
                        filteredList = filteredList.filter { it.categoryId == selectedCategoryId }
                    }
                    if (searchQuery.isNotEmpty()) {
                        filteredList = filteredList.filter { product ->
                            product.name.contains(searchQuery, ignoreCase = true)
                        }
                    }
                    DataResult.Success(filteredList)
                }

                else -> productsResult
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = DataResult.Loading
            )
    }
}