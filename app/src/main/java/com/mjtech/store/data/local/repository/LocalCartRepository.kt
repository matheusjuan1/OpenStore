package com.mjtech.store.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.mjtech.store.domain.model.Product

/**
 * CartRepository é um Singleton que gerencia o estado global do carrinho de compras.
 * Ele mantém um mapa de produtos e suas quantidades, e expõe LiveData para observadores.
 */
object LocalCartRepository {

    private val _cartItems = MutableLiveData<MutableMap<String, Int>>(mutableMapOf())

    val cartItems: MutableLiveData<MutableMap<String, Int>> get() = _cartItems

    val totalItemsInCart: LiveData<Int> = _cartItems.map { cartMap ->
        cartMap.values.sum()
    }

    val cartTotalValue: LiveData<Double> = _cartItems.map { cartMap ->

        var total = 0.0
//        cartMap.forEach { (productId, quantity) ->
//            val product = ProductRepositoryMock.getProductById(productId)
//            if (product != null) {
//                total += product.price * quantity
//            }
//        }
        total
    }


    fun addItem(product: Product) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        val newCart = currentCart.toMutableMap()

//        val currentQuantity = newCart[product.id] ?: 0
//        newCart[product.id] = currentQuantity + 1

        _cartItems.value = newCart
    }

    fun removeItem(product: Product) {
        val currentCart = _cartItems.value ?: return
        val newCart = currentCart.toMutableMap()

//        val currentQuantity = newCart[product.id] ?: 0
//        if (currentQuantity > 0) {
//            val newQuantity = currentQuantity - 1
//            if (newQuantity == 0) {
//                newCart.remove(product.id)
//            } else {
//                newCart[product.id] = newQuantity
//            }
//        }

        _cartItems.value = newCart
    }

    /**
     * Obtém a quantidade atual de um produto específico no carrinho.
     * @param productId O ID do produto.
     * @return A quantidade do produto no carrinho, ou 0 se não estiver presente.
     */
    fun getQuantity(productId: Int): Int {
        return _cartItems.value?.get(productId.toString()) ?: 0
    }


    fun clearCart() {
        _cartItems.value = mutableMapOf()
    }
}
