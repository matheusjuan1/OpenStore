package br.com.projetoacbr.food.ui.products

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.OptIn
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import br.com.projetoacbr.food.R
import br.com.projetoacbr.food.databinding.ActivityHomeBinding
import br.com.projetoacbr.food.view.custom.CustomDrawerContentView
import br.com.projetoacbr.food.view.dialog.CartBottomSheetDialog
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.chip.Chip
import com.google.android.material.navigation.NavigationView

class ProductsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var productsAdapter: ProductsAdapter
    private val productsViewModel: ProductsViewModel by viewModels()

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

        initValues()
        initObservers()
        initListeners()
    }

    @SuppressLint("UnsafeOptInUsageError", "SetTextI18n")
    private fun initValues() {
        productsAdapter = ProductsAdapter(
            lifecycleOwner = this@ProductsActivity,
            onAddItemClicked = { product -> productsViewModel.addItemToCart(product) },
            onRemoveItemClicked = { product -> productsViewModel.removeItemFromCart(product) }
        )

        binding.recyclerViewProducts.layoutManager = GridLayoutManager(this, calculateNoOfColumns())
        binding.recyclerViewProducts.adapter = productsAdapter

        binding.customDrawer.setItemActive(CustomDrawerContentView.ItemDrawer.HOME)
    }

    @OptIn(ExperimentalBadgeUtils::class)
    private fun initObservers() {
        productsViewModel.filteredProducts.observe(this) { filteredProducts ->
            productsAdapter.submitList(filteredProducts)
        }

        productsViewModel.totalCartItemsCount.observe(this) { count ->
            if (count > 0) {
                if (cartBadge == null) {
                    cartBadge = BadgeDrawable.create(this).apply {
                        isVisible = true
                        backgroundColor = resources.getColor(R.color.red, theme)
                        badgeTextColor = resources.getColor(R.color.white, theme)
                        maxCharacterCount = 3
                    }
                    binding.customAppBar.ivCartIcon.post {
                        cartBadge?.let { badge ->
                            BadgeUtils.attachBadgeDrawable(
                                badge,
                                binding.customAppBar.ivCartIcon,
                                binding.customAppBar.flCartIconContainer
                            )
                        }
                    }
                }
                cartBadge?.number = count
                cartBadge?.isVisible = true

                if (binding.fabCart.isGone) {
                    binding.fabCart.show()
                }
            } else {
                cartBadge?.isVisible = false
                binding.customAppBar.ivCartIcon.post {
                    cartBadge?.let { badge ->
                        BadgeUtils.detachBadgeDrawable(
                            badge,
                            binding.customAppBar.toolbar,
                            binding.customAppBar.flCartIconContainer.id
                        )
                    }
                }
                cartBadge = null
                if (binding.fabCart.isVisible) {
                    binding.fabCart.hide()
                }
            }
        }
    }

    private fun initListeners() {
        binding.customAppBar.ivCartIcon.setOnClickListener {
            val currentCartItemCount = productsViewModel.totalCartItemsCount.value ?: 0
            if (currentCartItemCount > 0) {
                showCartDialog()
            } else {
                Toast.makeText(this, getString(R.string.empty_cart), Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.fabCart.setOnClickListener {
            showCartDialog()
        }

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

        binding.chipGroupCategories.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChipId = checkedIds[0]
                val selectedChip = group.findViewById<Chip>(selectedChipId)
                val selectedCategory = selectedChip.text.toString()

                productsViewModel.filterByCategory(selectedCategory)
            }
        }

        drawerLayout = binding.drawerLayout

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.customAppBar.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawable = toggle.drawerArrowDrawable
        val tintColor = ContextCompat.getColor(this, R.color.white)
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_ATOP)

        binding.customDrawer.setNavigationListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {

            }

            R.id.nav_settings -> {

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