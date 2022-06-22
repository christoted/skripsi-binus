package com.example.project_skripsi.utils.service.storage

import android.content.Context
import android.util.Log
import android.widget.Toast

class StorageSP {

    companion object {

        private const val SP_KEY = "halohalo"
        const val SP_EMAIL = "sp_email"
        const val SP_PASSWORD = "sp_password"
        const val SP_LOGIN_AS = "sp_login_as"

        fun getString(context: Context, spKey: String) : String? {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            return sharedPref.getString(spKey, "")
        }

        fun setString(context: Context, spKey: String, value: String) {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putString(spKey, value)
                apply()
            }
        }

        fun getInt(context: Context, spKey: String, default: Int) : Int {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
//            Toast.makeText(context, "get (${spKey}) => ${sharedPref.getInt(spKey, default)}", Toast.LENGTH_LONG).show()
            return sharedPref.getInt(spKey, default)
        }

        fun setInt(context: Context, spKey: String, value: Int) {
            val sharedPref = context.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE)
            with (sharedPref.edit()) {
                putInt(spKey, value)
                apply()
            }
//            Toast.makeText(context, "set (${spKey}) => $value", Toast.LENGTH_LONG).show()
        }
    }
}