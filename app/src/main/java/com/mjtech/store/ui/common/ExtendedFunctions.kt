package com.mjtech.store.ui.common

import java.text.NumberFormat
import java.util.Locale

fun Double.currencyFormat(): String {
    val locale = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()
    return NumberFormat.getCurrencyInstance(locale).format(this)
}

fun Double.format(): String {
    val locale = Locale.Builder()
        .setLanguage("pt")
        .setRegion("BR")
        .build()
    return NumberFormat.getInstance(locale).format(this)
}