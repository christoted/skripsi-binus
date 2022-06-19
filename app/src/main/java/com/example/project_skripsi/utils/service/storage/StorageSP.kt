package com.example.project_skripsi.utils.service.storage

import android.app.Activity
import android.content.Context

class StorageSP {

    companion object {

        private const val SP_KEY = "halohalo"
        const val SP_EMAIL = "sp_email"
        const val SP_PASSWORD = "sp_password"
        const val SP_LOGIN_AS = "sp_login_as"

        fun get(activity: Activity, spKey: String) : String? {
            val sharedPref = activity.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            return sharedPref.getString(spKey, "")
        }

        fun set(activity: Activity, spKey: String, value: String) {
            val sharedPref = activity.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(spKey, value)
                apply()
            }
        }

        fun getInt(activity: Activity, spKey: String, default: Int) : Int {
            val sharedPref = activity.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            return sharedPref.getInt(spKey, default)
        }

        fun setInt(activity: Activity, spKey: String, value: Int) {
            val sharedPref = activity.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putInt(spKey, value)
                apply()
            }
        }
    }
}