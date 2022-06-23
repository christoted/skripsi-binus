package com.example.project_skripsi.utils.helper

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class CurrencyHelper {

    companion object {

        fun toRupiah(value: Int): String {
            val formatRp = DecimalFormatSymbols()
            formatRp.currencySymbol = "Rp"
            formatRp.monetaryDecimalSeparator = ','
            formatRp.groupingSeparator = '.'

            val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
            kursIndonesia.decimalFormatSymbols = formatRp
            return kursIndonesia.format(value)
        }

    }
}