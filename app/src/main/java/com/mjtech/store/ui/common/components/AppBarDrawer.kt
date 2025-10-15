package com.mjtech.store.ui.common.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.mjtech.store.R
import com.mjtech.store.databinding.LayoutDrawerBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class AppBarDrawer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class ItemDrawer(val id: Int) {
        HOME(R.id.nav_home),
    }

    private val binding: LayoutDrawerBinding =
        LayoutDrawerBinding.inflate(LayoutInflater.from(context), this, true)
    private val navView: NavigationView = binding.navView

    fun setNavigationListener(listener: OnNavigationItemSelectedListener) {
        navView.setNavigationItemSelectedListener(listener)
    }

    fun setItemActive(itemDrawer: ItemDrawer) {
        navView.menu.findItem(itemDrawer.id)?.isChecked = true
    }
}