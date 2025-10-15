package com.mjtech.store.ui.products

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mjtech.store.data.local.repository.LocalCartRepository
import com.mjtech.store.databinding.ItemProductCardBinding
import com.mjtech.store.domain.model.Product

class ProductsAdapter(
    private val onAddItemClicked: (Product) -> Unit,
    private val onRemoveItemClicked: (Product) -> Unit
) : ListAdapter<Product, ProductsAdapter.ProductViewHolder>(ProductDiffCallback()) {

    class ProductViewHolder(
        private val binding: ItemProductCardBinding,
        private val onAddItemClicked: (Product) -> Unit,
        private val onRemoveItemClicked: (Product) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentProduct: Product? = null

        init {
            binding.btnAddItem.setOnClickListener {
                currentProduct?.let { product ->
                    onAddItemClicked(product)
                }
            }
            binding.btnRemoveItem.setOnClickListener {
                currentProduct?.let { product ->
                    onRemoveItemClicked(product)
                }
            }

        }

        @SuppressLint("DefaultLocale", "SetTextI18n")
        fun bind(product: Product) {
            currentProduct = product
            binding.tvProductName.text = product.name
            binding.tvProductPrice.text = "R$ ${String.format("%.2f", product.price)}"

//            if (product.image != null) {
//                binding.ivProductImage.setImageResource(product.image)
//            } else {
//                binding.ivProductImage.setImageResource(R.drawable.ic_launcher_foreground)
//            }

            val quantityInCart = LocalCartRepository.getQuantity(product.id)
            binding.tvItemQuantity.text = quantityInCart.toString()
            binding.btnRemoveItem.isEnabled = quantityInCart > 0
        }
    }

    class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name &&
                    oldItem.price == newItem.price &&
                    oldItem.imageUrl == newItem.imageUrl &&
                    oldItem.categoryId == newItem.categoryId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding, onAddItemClicked, onRemoveItemClicked)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}