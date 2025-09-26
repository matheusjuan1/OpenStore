package com.mjtech.store.di

import com.mjtech.store.data.local.repository.LocalProductsRepository
import com.mjtech.store.domain.repository.ProductsRepository
import com.mjtech.store.ui.products.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val storeModules = module {

    single<ProductsRepository> { LocalProductsRepository(get()) }

    viewModel { ProductsViewModel(get()) }
}