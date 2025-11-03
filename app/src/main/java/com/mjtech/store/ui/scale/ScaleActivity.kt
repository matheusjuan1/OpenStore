package com.mjtech.store.ui.scale

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.mjtech.store.R
import com.mjtech.store.databinding.ActivityScaleBinding
import com.mjtech.store.domain.common.Result
import com.mjtech.store.domain.scale.model.PriceSetting
import com.mjtech.store.ui.common.components.LoadingDialog
import com.mjtech.store.ui.common.components.SnackbarType
import com.mjtech.store.ui.common.components.showSnackbar
import com.mjtech.store.ui.common.currencyFormat
import com.mjtech.store.ui.common.format
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScaleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScaleBinding

    private val scaleViewModel: ScaleViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScaleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initListeners()
        initObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        scaleViewModel.deactivate()
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.initialLoading.collect { isLoading ->
                    if (isLoading) {
                        LoadingDialog.show(supportFragmentManager)
                    } else {
                        LoadingDialog.hide(supportFragmentManager)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.price }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {}

                            is Result.Error -> {
                                showSnackbar(
                                    binding.root, getString(R.string.error_price_scale),
                                    SnackbarType.ERROR
                                )
                            }

                            is Result.Success -> {
                                setupPriceDisplay(state.data)
                            }
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.initialize }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {}
                            is Result.Error -> {
                                showSnackbar(binding.root, state.error, SnackbarType.ERROR)
                            }

                            is Result.Success -> {
                                scaleViewModel.configScale()
                            }
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.configScale }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {}
                            is Result.Error -> {
                                showSnackbar(binding.root, state.error, SnackbarType.ERROR)
                            }

                            is Result.Success -> {
                                scaleViewModel.activate()
                            }
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.activate }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {}
                            is Result.Error -> {
                                showSnackbar(binding.root, state.error, SnackbarType.ERROR)
                            }

                            is Result.Success -> {}
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.weight }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {
                            }

                            is Result.Error -> {
                                showSnackbar(
                                    binding.root, getString(R.string.error_get_weight),
                                    SnackbarType.ERROR
                                )
                            }

                            is Result.Success -> {
                                scaleViewModel.calculatePlateValue()
                            }
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState.collect { state ->
                    state.calculationError?.let { error ->
                        showSnackbar(binding.root, error, SnackbarType.ERROR)
                    }
                    if (state.totalValue > 0.0) {
                        setupResultCard(
                            state.totalValue,
                            (state.weight as? Result.Success)?.data ?: 0.0
                        )
                    } else {
                        binding.bottomPanelContainer.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.fabBack.setOnClickListener {
            finish()
        }

        binding.btnCalculate.setOnClickListener {
            scaleViewModel.getWeight()
        }

        binding.btnAddToCart.setOnClickListener {

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPriceDisplay(priceSetting: PriceSetting) {
        binding.tvPrice.text =
            "Preço por ${priceSetting.unit}: ${priceSetting.pricePerKg.currencyFormat()}"
    }

    @SuppressLint("SetTextI18n")
    private fun setupResultCard(totalValue: Double, weight: Double) {
        binding.bottomPanelContainer.visibility = View.VISIBLE

        binding.tvWeight.text = getString(R.string.peso_kg, weight.format())

        binding.tvFinalValue.text = totalValue.currencyFormat()
    }
}