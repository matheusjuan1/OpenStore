package com.mjtech.pos.di

import com.mjtech.pos.data.repository.LocalProductsRepository
import com.mjtech.pos.domain.repository.ProductsRepository
import com.mjtech.pos.ui.products.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val acbrFoodModules = module {

    single<ProductsRepository> { LocalProductsRepository(get()) }

    viewModel { ProductsViewModel(get()) }
}