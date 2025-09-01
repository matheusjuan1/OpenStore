package br.com.projetoacbr.food.ui.products

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.projetoacbr.food.R
import br.com.projetoacbr.food.domain.model.Product
import br.com.projetoacbr.food.repository.CartRepository

class ProductsViewModel : ViewModel() {

    private val _allProducts = MutableLiveData<List<Product>>()

    private val _filteredProducts = MutableLiveData<List<Product>>()
    val filteredProducts: LiveData<List<Product>> get() = _filteredProducts

    private var currentCategoryFilter: String = "Todos"
    private var currentSearchQuery: String = ""

    val totalCartItemsCount: LiveData<Int> = CartRepository.totalItemsInCart

    val cartTotalValue: LiveData<Double> = CartRepository.cartTotalValue

    init {
        loadInitialProducts()
    }

    private fun loadInitialProducts() {
        val products = mutableListOf<Product>()
        products.add(Product("1", "Camiseta ACBr", 99.99, R.drawable.img_camiseta, "Vestuário"))
        products.add(Product("2", "Boné ACBr", 49.99, R.drawable.img_cap, "Vestuário"))
        products.add(Product("3", "Caneca ACBr", 34.99, R.drawable.img_caneca, "Acessórios"))
        products.add(
            Product(
                "4",
                "Garrafa 500ml ACBr",
                49.99,
                R.drawable.img_garrafa,
                "Acessórios"
            )
        )
        products.add(Product("5", "Mochila ACBr", 140.00, R.drawable.img_backpack, "Acessórios"))
        products.add(Product("6", "Caderno ACBr", 25.00, R.drawable.img_book, "Papelaria"))


        _allProducts.value = products
        applyFilters()
    }

    fun filterByCategory(category: String) {
        currentCategoryFilter = category
        applyFilters()
    }

    fun filterBySearchQuery(query: String) {
        currentSearchQuery = query
        applyFilters()
    }

    private fun applyFilters() {
        val currentProducts = _allProducts.value ?: return

        var filteredList = currentProducts

        if (currentCategoryFilter != "Todos" && currentCategoryFilter.isNotEmpty()) {
            filteredList = filteredList.filter { it.category == currentCategoryFilter }
        }

        if (currentSearchQuery.isNotEmpty()) {
            filteredList = filteredList.filter { product ->
                product.name.contains(currentSearchQuery, ignoreCase = true) ||
                        product.category.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        _filteredProducts.value = filteredList
    }

    fun addItemToCart(product: Product) {
        CartRepository.addItem(product)
    }

    fun removeItemFromCart(product: Product) {
        CartRepository.removeItem(product)
    }
}