package com.kobbi.project.coronamask.util

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class SharedPrefHelper private constructor() {
    companion object {
        private const val KEY_PREFERENCE = "_Preference"

        const val KEY_DB_INITIALIZED = "db_init"
        const val KEY_DB_UPDATE_TIME = "db_update_time"

        @JvmStatic
        fun setBool(context: Context, key: String, value: Boolean) {
            getPreference(context).edit().run {
                putBoolean(key, value)
                apply()
            }
        }

        @JvmStatic
        fun getBool(context: Context, key: String, defValue: Boolean = false): Boolean {
            return getPreference(context).getBoolean(key, defValue)
        }

        @JvmStatic
        fun setInt(context: Context, key: String, value: Int) {
            getPreference(context).edit().run {
                putInt(key, value)
                apply()
            }
        }

        @JvmStatic
        fun getInt(context: Context, key: String, defValue: Int = Int.MIN_VALUE): Int {
            return getPreference(context).getInt(key, defValue)
        }

        @JvmStatic
        fun setLong(context: Context, key: String, value: Long) {
            getPreference(context).edit().run {
                putLong(key, value)
                apply()
            }
        }

        @JvmStatic
        fun getLong(context: Context, key: String, defValue: Long = 0L): Long {
            return getPreference(context).getLong(key, defValue)
        }

        @JvmStatic
        fun setDate(context: Context, key: String, value: Date?) {
            value?.run {
                getPreference(context).edit().run {
                    putLong(key, value.time)
                    apply()
                }
            }
        }

        @JvmStatic
        fun getDate(context: Context, key: String): Date? {
            return getPreference(context).getLong(key, 0L).let { value ->
                if (value != 0L)
                    Date().apply {
                        time = value
                    }
                else
                    null
            }
        }

        @JvmStatic
        fun registerPrefChangeListener(
            context: Context,
            listener: SharedPreferences.OnSharedPreferenceChangeListener
        ) {
            getPreference(context).registerOnSharedPreferenceChangeListener(listener)
        }

        @JvmStatic
        fun unregisterPrefChangeListener(
            context: Context,
            listener: SharedPreferences.OnSharedPreferenceChangeListener
        ) {
            getPreference(context).unregisterOnSharedPreferenceChangeListener(listener)
        }

        private fun getPreference(context: Context): SharedPreferences {
            return context.applicationContext.getSharedPreferences(
                "${context.packageName}${KEY_PREFERENCE}", Context.MODE_PRIVATE
            )
        }
    }
}