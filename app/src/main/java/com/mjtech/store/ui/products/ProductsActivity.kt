package com.mjtech.store.ui.products

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.chip.ChipGroup
import com.google.android.material.navigation.NavigationView
import com.mjtech.store.R
import com.mjtech.store.databinding.ActivityHomeBinding
import com.mjtech.store.domain.common.DataResult
import com.mjtech.store.domain.model.Category
import com.mjtech.store.ui.common.components.AppBarDrawer
import com.mjtech.store.ui.common.components.CartBottomSheetDialog
import com.mjtech.store.ui.common.components.LoadingDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
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
    }

    private fun initComponents() {
        setupDrawer()
        setupBadgeCart()
        setupSearch()
        setupProductsList()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.uiState
                    .map { it.categories }
                    .distinctUntilChanged()
                    .collect { categoriesResult ->
                        when (categoriesResult) {
                            is DataResult.Success -> {
                                createCategoriesChips(categoriesResult.data)
                                setCategorySelectionListener()
                            }

                            is DataResult.Error -> {
                                Toast.makeText(
                                    this@ProductsActivity,
                                    getString(R.string.error_loading_categories),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(
                                    TAG,
                                    categoriesResult.error
                                )
                            }

                            is DataResult.Loading -> {}
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.uiState
                    .map { it.addItemState }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is DataResult.Error -> {
                                Toast.makeText(
                                    this@ProductsActivity,
                                    getString(R.string.error_add_cart_item),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(
                                    TAG,
                                    state.error
                                )
                                productsViewModel.resetAddItemState()
                            }

                            is DataResult.Loading -> {}
                            is DataResult.Success -> {}
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.uiState
                    .map { it.removeItemState }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is DataResult.Error -> {
                                Toast.makeText(
                                    this@ProductsActivity,
                                    getString(R.string.error_remove_cart_item),
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(
                                    TAG,
                                    state.error
                                )
                                productsViewModel.resetRemoveItemState()
                            }

                            is DataResult.Loading -> {}
                            is DataResult.Success -> {}
                        }
                    }
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                productsViewModel.uiState.collect { uiState ->
                    // Verifica se algum dado está carregando
                    val isAnythingLoading =
                        uiState.categories is DataResult.Loading ||
                                uiState.products is DataResult.Loading

                    if (isAnythingLoading) {
                        LoadingDialog.show(supportFragmentManager)
                    } else {
                        lifecycleScope.launch {
                            delay(50)
                            LoadingDialog.hide(supportFragmentManager)
                        }
                    }

                    when (uiState.products) {
                        is DataResult.Success -> {
                            productsAdapter.submitList(uiState.products.data)
                        }

                        is DataResult.Error -> {
                            Toast.makeText(
                                this@ProductsActivity,
                                getString(R.string.error_loading_products),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e(
                                "ProductsActivity",
                                uiState.products.error
                            )
                        }

                        is DataResult.Loading -> {}
                    }
                }
            }
        }
    }

    // Navigation Drawer

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {}
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    // Cart

    private fun setupBadgeCart() {
        binding.appBar.ivCartIcon.setOnClickListener {
            val currentCartItemCount = 0
            if (currentCartItemCount > 0) {
                showCartDialog()
            } else {
                Toast.makeText(this, getString(R.string.empty_cart), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun showCartDialog() {
        val existingBottomSheet =
            supportFragmentManager.findFragmentByTag(CartBottomSheetDialog.Companion.TAG) as? CartBottomSheetDialog

        if (existingBottomSheet == null || !existingBottomSheet.isAdded) {
            val newBottomSheet = CartBottomSheetDialog()
            newBottomSheet.show(supportFragmentManager, CartBottomSheetDialog.Companion.TAG)
        }
    }

    // Search

    private fun setupSearch() {
        binding.searchTextInput.setEndIconOnClickListener {
            binding.searchEditText.setText("")
            productsViewModel.onSearchQueryChanged("")
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                productsViewModel.onSearchQueryChanged(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    // Categories Chips

    private fun createCategoriesChips(categories: List<Category>) {
        val categories = listOf(Category(id = "0", name = "Todos")) + categories
        binding.chipGroupCategories.removeAllViews()

        var firstChipId = -1

        categories.forEachIndexed { index, category ->
            val chip = getCategoryChip(category)
            binding.chipGroupCategories.addView(chip)
            if (index == 0) {
                firstChipId = chip.id
            }
        }

        if (firstChipId != -1) {
            binding.chipGroupCategories.check(firstChipId)
        }
    }

    private fun getCategoryChip(category: Category): Chip {
        val chip = Chip(this)

        val chipStyle = ChipDrawable.createFromAttributes(this, null, 0, R.style.StoreChip)
        chip.setChipDrawable(chipStyle)

        chip.setTextAppearanceResource(R.style.StoreChip)

        chip.id = View.generateViewId()
        chip.tag = category.id
        chip.text = category.name
        chip.isCheckable = true
        chip.isClickable = true

        val layoutParams = ChipGroup.LayoutParams(
            ChipGroup.LayoutParams.WRAP_CONTENT,
            ChipGroup.LayoutParams.WRAP_CONTENT
        )
        chip.layoutParams = layoutParams

        return chip
    }

    private fun setCategorySelectionListener() {
        binding.chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedCategoryIdChip = checkedIds[0]
                val selectedCategoryTagChip =
                    group.findViewById<Chip>(selectedCategoryIdChip).tag.toString()
                productsViewModel.onCategorySelected(selectedCategoryTagChip)
            }
        }
    }

    // Products RecyclerView

    private fun setupProductsList() {
        productsAdapter = ProductsAdapter(
            onAddItemClicked = { product ->
                productsViewModel.onAddProductToCart(product)
            },
            onRemoveItemClicked = { product ->
                productsViewModel.onRemoveProductFromCart(product)
            }
        )

        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
        binding.recyclerViewProducts.adapter = productsAdapter
    }

    private fun calculateNoOfColumns(): Int {
        val displayMetrics = resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        val noOfColumns = (dpWidth / 180).toInt()
        return if (noOfColumns < 2) 2 else noOfColumns
    }

    // Utils

    companion object {
        const val TAG = "ProductsActivity"
    }
}