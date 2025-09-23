package com.mjtech.pos.ui.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mjtech.pos.R
import com.mjtech.pos.databinding.LayoutCustomDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class CustomDrawerContentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class ItemDrawer(val id: Int) {
        HOME(R.id.nav_home),
        PRINT(R.id.nav_print),
        SETTINGS(R.id.nav_settings)
    }

    private val binding: LayoutCustomDrawerBinding =
        LayoutCustomDrawerBinding.inflate(LayoutInflater.from(context), this, true)
    private val navView: NavigationView = binding.navView

    /**
     * Define o listener para os cliques nos itens do menu de navegação.
     */
    fun setNavigationListener(listener: OnNavigationItemSelectedListener) {
        navView.setNavigationItemSelectedListener(listener)
    }

    fun setAcquirerName(acquirerName: String) {
        binding.navFooter.tvAcquirerInfo.text =
            context.getString(R.string.acquirer_name, acquirerName)
    }

    fun setItemActive(itemDrawer: ItemDrawer) {
        navView.menu.findItem(itemDrawer.id)?.isChecked = true
    }
}