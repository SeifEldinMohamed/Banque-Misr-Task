package com.seif.banquemisrttask.data.datasources.localdatasource.sharedprefrence

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

        fun readLastTimeDataFetched(key: String, defValue: Long) =
            sharedPref?.let { sharedPref ->
                defValue.let { value ->
                    sharedPref.getLong(key, value)
                }
            }

        fun writeLastTimeDataFetched(key: String, value: Long) {
            sharedPref?.edit()?.let { prefsEditor ->
                value.let { value ->
                    prefsEditor.putLong(key, value)
                    prefsEditor.apply()
                }
            }
        }
    }
}