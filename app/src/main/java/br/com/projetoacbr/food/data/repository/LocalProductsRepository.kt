package br.com.projetoacbr.food.data.repository

import android.content.Context
import android.util.Log
import br.com.projetoacbr.food.data.common.AppJson
import br.com.projetoacbr.food.domain.common.DataResult
import br.com.projetoacbr.food.domain.model.Category
import br.com.projetoacbr.food.domain.model.Product
import br.com.projetoacbr.food.domain.repository.ProductsRepository
import java.io.IOException

class LocalProductsRepository(private val context: Context) : ProductsRepository {

    private val TAG = "LocalProductsRepository"

    override suspend fun getCategories(): DataResult<List<Category>> {
        val fileName = "mock_categories.json"

        return try {
            val jsonString = loadJsonFromAssets(fileName)
            if (jsonString == null) {
                Log.w(TAG, "Arquivo mock não encontrado para: $fileName. Retornando lista vazia.")
                DataResult.Success(emptyList())
            } else {
                val categories = AppJson.decodeFromString<List<Category>>(jsonString)
                DataResult.Success(categories)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar categorias: ${e.localizedMessage}", e)
            DataResult.Error("${e.localizedMessage}")
        }
    }

    override suspend fun getProducts(): DataResult<List<Product>> {
        val fileName = "mock_products.json"

        return try {
            val jsonString = loadJsonFromAssets(fileName)
            if (jsonString == null) {
                Log.w(TAG, "Arquivo mock não encontrado para: $fileName. Retornando lista vazia.")
                DataResult.Success(emptyList())
            } else {
                val products = AppJson.decodeFromString<List<Product>>(jsonString)
                DataResult.Success(products)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao carregar produtos: ${e.localizedMessage}", e)
            DataResult.Error("${e.localizedMessage}")
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