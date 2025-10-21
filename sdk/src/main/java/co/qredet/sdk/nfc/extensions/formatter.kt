package co.qredet.sdk.nfc.extensions

import java.text.NumberFormat
import java.util.*

fun Double.formatAsCurrency(currencyCode: String = "USD"): String {
    val format = NumberFormat.getCurrencyInstance(Locale.getDefault())
    format.currency = Currency.getInstance(currencyCode)
    return format.format(this)
}

fun String.formatCardNumber(): String {
    return this.replace("(.{4})".toRegex(), "$1 ").trim()
}

fun String.maskCardNumber(): String {
    if (this.length < 8) return this
    val start = this.substring(0, 4)
    val end = this.substring(this.length - 4)
    val middle = "*".repeat(this.length - 8)
    return "$start$middle$end"
}
