package com.mjtech.store.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Product
import com.mjtech.store.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(private val productsRepository: ProductsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductsUiState())
    val uiState: StateFlow<ProductsUiState> = _uiState
    private val _allProducts = MutableStateFlow<DataResult<List<Product>>>(DataResult.Loading)

    init {
        getCategories()
        getAllProducts()
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

    private fun getAllProducts() {
        viewModelScope.launch {
            productsRepository.getProducts().collect { result ->
                _allProducts.value = result
                applyFilters()
            }
        }
    }

    fun onCategorySelected(categoryId: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedCategoryId = categoryId)
        }
        applyFilters()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = query)
        }
        applyFilters()
    }

    private fun applyFilters() {
        val allProducts = _allProducts.value
        val categoryId = _uiState.value.selectedCategoryId
        val searchQuery = _uiState.value.searchQuery

        if (allProducts is DataResult.Success) {
            var filteredList = allProducts.data

            if (categoryId != 0) {
                filteredList = filteredList.filter { it.categoryId == categoryId }
            }

            if (searchQuery.isNotEmpty()) {
                filteredList = filteredList.filter { product ->
                    product.name.contains(searchQuery, ignoreCase = true)
                }
            }

            _uiState.update { currentState ->
                currentState.copy(products = DataResult.Success(filteredList))
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(products = allProducts)
            }
        }
    }
}