package com.seif.banquemisrttask.data.sharedprefrence

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AppSharedPreference {
    companion object {
        private var sharedPref: SharedPreferences? = null

        fun init(context: Context) {
            if (sharedPref == null)
                sharedPref =
                    context.getSharedPreferences(context.packageName, Activity.MODE_PRIVATE)
        }

        fun readIsFirstTime(key: String, defValue: Boolean) =
            sharedPref?.let { sharedPref ->
                defValue.let { value ->
                    sharedPref.getBoolean(key, value)
                }
            }

        fun writeIsFirstTime(key: String, value: Boolean) {
            sharedPref?.edit()?.let { prefsEditor ->
                value.let { value ->
                    prefsEditor.putBoolean(key, value)
                    prefsEditor.apply()
                }
            }
        }
    }
}