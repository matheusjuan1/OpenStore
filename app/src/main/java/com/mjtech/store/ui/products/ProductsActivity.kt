package com.mjtech.store.ui.products

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView
import com.mjtech.store.R
import com.mjtech.store.databinding.ActivityHomeBinding
import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.ui.common.components.CartBottomSheetDialog
import com.mjtech.store.ui.common.components.AppBarDrawer
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var productsAdapter: ProductsAdapter
    private val productsViewModel: ProductsViewModel by viewModel()

    private var cartBadge: BadgeDrawable? = null
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initComponents()
        initObservers()
        initValues()
    }

    private fun initComponents() {
        setupDrawer()
        setupCategories()
    }

    @SuppressLint("UnsafeOptInUsageError", "SetTextI18n")
    private fun initValues() {

//        productsAdapter = ProductsAdapter(
//            lifecycleOwner = this@ProductsActivity,
//            onAddItemClicked = { product -> productsViewModel.addItemToCart(product) },
//            onRemoveItemClicked = { product -> productsViewModel.removeItemFromCart(product) }
//        )

//        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
//        binding.recyclerViewProducts.adapter = productsAdapter
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.uiState.collect { uiState ->
                    when (uiState.categories) {
                        is DataResult.Loading -> {
                            Log.d("ProductsActivity", "Loading categories...")
                        }

                        is DataResult.Success -> {
                            Log.d(
                                "ProductsActivity",
                                "Categories loaded: ${uiState.categories.data.size} items"
                            )
                        }

                        is DataResult.Error -> {
                            Log.e(
                                "ProductsActivity",
                                "Error loading categories: ${uiState.categories.error}"
                            )
                        }
                    }

                    when (uiState.products) {
                        is DataResult.Loading -> {
                            Log.d("ProductsActivity", "Loading products...")
                        }

                        is DataResult.Success -> {
                            Log.d(
                                "ProductsActivity",
                                "Products loaded: ${uiState.products.data.size} items"
                            )
                        }

                        is DataResult.Error -> {
                            Log.e(
                                "ProductsActivity",
                                "Error loading products: ${uiState.products.error}"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setupDrawer() {
        drawerLayout = binding.drawerLayout

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBar.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawable = toggle.drawerArrowDrawable
        val tintColor = ContextCompat.getColor(this, R.color.white)
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)

        binding.appBarDrawer.setNavigationListener(this)

        binding.appBarDrawer.setItemActive(AppBarDrawer.ItemDrawer.HOME)
    }

    private fun setupBadgeCart() {
        //        binding.customAppBar.ivCartIcon.setOnClickListener {
//            val currentCartItemCount = 0
//            if (currentCartItemCount > 0) {
//                showCartDialog()
//            } else {
//                Toast.makeText(this, getString(R.string.empty_cart), Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
    }

    private fun setupCategories() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChipId = checkedIds[0]
                val selectedChip = group.findViewById<Chip>(selectedChipId)
            }
        }
    }

    private fun setupSearch() {
        binding.searchTextInput.setEndIconOnClickListener {
            binding.searchEditText.setText("")
            productsViewModel.filterBySearchQuery("")
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productsViewModel.filterBySearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                // TODO
            }

            R.id.nav_settings -> {
                // TODO
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showCartDialog() {
        val existingBottomSheet =
            supportFragmentManager.findFragmentByTag(CartBottomSheetDialog.Companion.TAG) as? CartBottomSheetDialog

        if (existingBottomSheet == null || !existingBottomSheet.isAdded) {
            val newBottomSheet = CartBottomSheetDialog()
            newBottomSheet.show(supportFragmentManager, CartBottomSheetDialog.Companion.TAG)
        }
    }


    private fun calculateNoOfColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val noOfColumns = (dpWidth / 180).toInt()
        return if (noOfColumns < 2) 2 else noOfColumns
    }
}