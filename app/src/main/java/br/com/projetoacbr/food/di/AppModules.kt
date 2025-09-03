package br.com.projetoacbr.food.di

import br.com.projetoacbr.food.data.repository.LocalProductsRepository
import br.com.projetoacbr.food.domain.repository.ProductsRepository
import br.com.projetoacbr.food.ui.products.ProductsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val acbrFoodModules = module {

    single<ProductsRepository> { LocalProductsRepository(get()) }

    viewModel { ProductsViewModel(get()) }
}