package com.mjtech.store.ui.scale

import android.annotation.SuppressLint
import android.os.Bundle
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
import kotlinx.coroutines.delay
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

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.price }
                    .distinctUntilChanged()
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {
                                LoadingDialog.show(supportFragmentManager)
                            }

                            is Result.Error -> {
                                lifecycleScope.launch {
                                    delay(50)
                                    LoadingDialog.hide(supportFragmentManager)
                                }
                                showSnackbar(
                                    binding.root, "Não foi possível obter o preço.",
                                    SnackbarType.ERROR
                                )
                            }

                            is Result.Success -> {
                                lifecycleScope.launch {
                                    delay(50)
                                    LoadingDialog.hide(supportFragmentManager)
                                }
                                setupPriceDisplay(state.data)
                            }
                        }
                    }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                scaleViewModel.uiState
                    .map { it.weight }
                    .collect { state ->
                        when (state) {
                            is Result.Loading -> {
//                                LoadingDialog.show(supportFragmentManager)
                            }

                            is Result.Error -> {
//                                lifecycleScope.launch {
//                                    delay(50)
//                                    LoadingDialog.hide(supportFragmentManager)
//                                }
                                showSnackbar(
                                    binding.root, "Não foi possível obter o peso.",
                                    SnackbarType.ERROR
                                )
                            }

                            is Result.Success -> {
//                                lifecycleScope.launch {
//                                    delay(50)
//                                    LoadingDialog.hide(supportFragmentManager)
//                                }
                                showSnackbar(
                                    binding.root,
                                    "Peso obtido com sucesso!\nPeso: ${state.data} kg",
                                    SnackbarType.SUCCESS
                                )
                            }
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
            // Abrir dialogo de adicionais ou finalizar compra
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupPriceDisplay(priceSetting: PriceSetting) {
        binding.tvPrice.text =
            "Preço por ${priceSetting.unit}: ${priceSetting.pricePerKg.currencyFormat()}"
    }
}