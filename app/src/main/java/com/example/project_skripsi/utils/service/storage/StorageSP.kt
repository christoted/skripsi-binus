package com.example.project_skripsi.utils.service.storage

import android.content.Context

class StorageSP {

    companion object {

        private const val SP_KEY = "halohalo"
        const val SP_EMAIL = "sp_email"
        const val SP_PASSWORD = "sp_password"
        const val SP_LOGIN_AS = "sp_login_as"
        const val SP_DAILY_NOTIFICATION = "sp_daily_notification"

        fun getString(context: Context, spKey: String): String? {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            return sharedPref.getString(spKey, "")
        }

        fun setString(context: Context, spKey: String, value: String) {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(spKey, value)
                apply()
            }
        }

        fun getInt(context: Context, spKey: String, default: Int): Int {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            return sharedPref.getInt(spKey, default)
        }

        fun setInt(context: Context, spKey: String, value: Int) {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putInt(spKey, value)
                apply()
            }
        }

        fun getBoolean(context: Context, spKey: String, default: Boolean): Boolean {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            return sharedPref.getBoolean(spKey, default)
        }

        fun setBoolean(context: Context, spKey: String, value: Boolean) {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putBoolean(spKey, value)
                apply()
            }
        }
    }
}