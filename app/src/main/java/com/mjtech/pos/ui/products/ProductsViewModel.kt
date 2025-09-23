package com.mjtech.pos.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.pos.data.repository.CartRepository
import com.mjtech.pos.domain.model.Product
import com.mjtech.pos.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(private val productsRepository: ProductsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState.asStateFlow()

    /** New
     *
     *
     */
    fun getCategories() {
        viewModelScope.launch {
            productsRepository.getCategories().collect { result ->
                _uiState.update { currentState ->
                    currentState.copy(categories = result)
                }
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            productsRepository.getProducts().collect { result ->
                _uiState.update { currentState ->
                    currentState.copy(products = result)
                }
            }
        }
    }


    /**
     * End New
     */

    fun filterByCategory(category: Int) {
//        currentCategoryFilter = category
        applyFilters()
    }

    fun filterBySearchQuery(query: String) {
//        currentSearchQuery = query
        applyFilters()
    }

    private fun applyFilters() {
//        val currentProducts = _allProducts.value ?: return

//        var filteredList = currentProducts

//        if (currentCategoryFilter != "Todos" && currentCategoryFilter.isNotEmpty()) {
//            filteredList = filteredList.filter { it.category == currentCategoryFilter }
//        }
//
//        if (currentSearchQuery.isNotEmpty()) {
//            filteredList = filteredList.filter { product ->
//                product.name.contains(currentSearchQuery, ignoreCase = true) ||
//                        product.category.contains(currentSearchQuery, ignoreCase = true)
//            }
//        }

//        _filteredProducts.value = filteredList
    }

    fun addItemToCart(product: Product) {
        CartRepository.addItem(product)
    }

    fun removeItemFromCart(product: Product) {
        CartRepository.removeItem(product)
    }
}