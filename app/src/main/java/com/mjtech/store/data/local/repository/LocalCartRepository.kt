package com.mjtech.store.data.local.repository

import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.CartItem
import com.mjtech.store.domain.model.Product
import com.mjtech.store.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


class LocalCartRepository : CartRepository {

    private val _cartItemsFlow = MutableStateFlow<MutableMap<String, CartItem>>(mutableMapOf())

    override fun addItem(product: Product): Flow<DataResult<Unit>> = flow {
        emit(DataResult.Loading)
        try {
            val productId = product.id
            val currentCart = _cartItemsFlow.value

            if (currentCart.containsKey(productId)) {
                val cartItem = currentCart.getValue(productId)

                currentCart[productId] = cartItem.copy(quantity = cartItem.quantity + 1)
            } else {
                currentCart[productId] = CartItem(product = product, quantity = 1)
            }
            _cartItemsFlow.value = currentCart
            emit(DataResult.Success(Unit))
        } catch (e: Exception) {
            emit(DataResult.Error("Erro ao adicionar item: ${e.message}"))
        }
    }

    override fun removeItem(product: Product): Flow<DataResult<Unit>> = flow {
        emit(DataResult.Loading)
        try {
            val productId = product.id
            val currentCart = _cartItemsFlow.value

            if (currentCart.containsKey(productId)) {
                val cartItem = currentCart.getValue(productId)

                if (cartItem.quantity > 1) {
                    currentCart[productId] = cartItem.copy(quantity = cartItem.quantity - 1)
                } else {
                    currentCart.remove(productId)
                }
                _cartItemsFlow.value = currentCart
                emit(DataResult.Success(Unit))
            } else {
                emit(DataResult.Error("Item não encontrado no carrinho"))
            }
        } catch (e: Exception) {
            emit(DataResult.Error("Erro ao remover item: ${e.message}"))
        }
    }

    override fun clearCart(): Flow<DataResult<Unit>> = flow {
        emit(DataResult.Loading)
        try {
            _cartItemsFlow.value.clear()

            _cartItemsFlow.value = mutableMapOf()
            emit(DataResult.Success(Unit))
        } catch (e: Exception) {
            emit(DataResult.Error("Erro ao limpar o carrinho: ${e.message}"))
        }
    }

    override fun getCartItems(): Flow<DataResult<List<CartItem>>> = flow {
        emit(DataResult.Loading)
        _cartItemsFlow
            .map { cartMap ->
                DataResult.Success(cartMap.values.toList())
            }
            .catch { e ->
                emit(DataResult.Error("Erro ao obter itens do carrinho: ${e.message}"))
            }
            .collectLatest {
                emit(it)
            }
    }

    override fun getTotalPrice(): Flow<DataResult<Double>> = flow {
        emit(DataResult.Loading)
        _cartItemsFlow
            .map { cartMap ->
                val totalPrice = cartMap.values.sumOf { item ->
                    item.product.price * item.quantity
                }
                DataResult.Success(totalPrice)
            }
            .catch { e ->
                emit(DataResult.Error("Erro ao calcular o preço total: ${e.message}"))
            }
            .collectLatest {
                emit(it)
            }
    }
}
