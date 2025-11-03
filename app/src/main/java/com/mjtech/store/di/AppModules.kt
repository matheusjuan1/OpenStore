package com.mjtech.store.di

import com.mjtech.store.data.mock.cart.repository.MockCartRepository
import com.mjtech.store.data.mock.products.repository.MockProductsRepository
import com.mjtech.store.data.mock.scale.repository.MockPricingRepository
import com.mjtech.store.domain.cart.repostitory.CartRepository
import com.mjtech.store.domain.payment.repository.PaymentProcessor
import com.mjtech.store.domain.products.repository.ProductsRepository
import com.mjtech.store.domain.scale.repository.PricingRepository
import com.mjtech.store.simulate.payment.SimulatePaymentProcessor
import com.mjtech.store.ui.cart.CartViewModel
import com.mjtech.store.ui.checkout.CheckoutViewModel
import com.mjtech.store.ui.products.ProductsViewModel
import com.mjtech.store.ui.scale.ScaleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val storeModules = module {

    // Repositories

    single<ProductsRepository> { MockProductsRepository(get()) }

    single<CartRepository> { MockCartRepository() }

    single<PaymentProcessor> { SimulatePaymentProcessor() }

    single<PricingRepository> { MockPricingRepository() }

    // ViewModels

    viewModel { ProductsViewModel(get()) }

    viewModel { CartViewModel(get()) }

    viewModel { CheckoutViewModel(get(), get()) }

    viewModel { ScaleViewModel(get(), get(), get()) }
}