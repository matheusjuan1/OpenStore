package com.mjtech.pos.data.repository

import android.content.Context
import android.util.Log
import com.mjtech.pos.data.common.AppJson
import com.mjtech.pos.domain.common.DataResult
import com.mjtech.pos.domain.model.Category
import com.mjtech.pos.domain.model.Product
import com.mjtech.pos.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class LocalProductsRepository(private val context: Context) : ProductsRepository {

    private val TAG = "LocalProductsRepository"

    override suspend fun getCategories(): Flow<DataResult<List<Category>>> = flow {
        emit(DataResult.Loading)
        val fileName = "mock_categories.json"

         try {
            val jsonString = loadJsonFromAssets(fileName)
            if (jsonString == null) {
                Log.e(TAG, "Arquivo mock não encontrado para: $fileName.")
                emit(DataResult.Error("Erro ao carregar categorias."))
            } else {
                val categories = AppJson.decodeFromString<List<Category>>(jsonString)
                emit(DataResult.Success(categories))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar categorias: ${e.localizedMessage}", e)
            emit(DataResult.Error("${e.localizedMessage}"))
        }
    }

    override suspend fun getProducts(): Flow<DataResult<List<Product>>> = flow {
        emit(DataResult.Loading)
        val fileName = "mock_products.json"

        try {
            val jsonString = loadJsonFromAssets(fileName)
            if (jsonString == null) {
                Log.e(TAG, "Arquivo mock não encontrado para: $fileName. Retornando lista vazia.")
                emit(DataResult.Error("Erro ao carregar produtos."))
            } else {
                val products = AppJson.decodeFromString<List<Product>>(jsonString)
                emit(DataResult.Success(products))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar produtos: ${e.localizedMessage}", e)
            emit(DataResult.Error("${e.localizedMessage}"))
        }
    }

    private fun loadJsonFromAssets(fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            Log.e(
                TAG,
                "Erro ao ler arquivo json '$fileName': ${e.localizedMessage}",
                e
            )
            null
        }
    }
}