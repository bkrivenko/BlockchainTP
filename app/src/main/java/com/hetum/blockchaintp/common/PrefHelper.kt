package com.hetum.blockchaintp.common

import android.content.Context
import android.content.SharedPreferences

class PrefHelper(context: Context) {

    private val PREF_FILENAME = "com.hetum.blockchaintp"
    private val TOKEN = "token"
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_FILENAME, 0)

    var token: String
        get() = pref.getString(TOKEN, "")!!
        set(value) = pref.edit().putString(TOKEN, value).apply()

    fun clearToken(){
        token = ""
    }
}