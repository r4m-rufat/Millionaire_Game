package com.codingwithrufat.millionarie.utils.internal_storage

import android.content.Context
import android.content.SharedPreferences




class PreferenceManager(context: Context) {

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences("name", Context.MODE_PRIVATE)


    fun putInt(key: String?, value: Int) {
        val editor = sharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String?): Int {
        return sharedPreferences!!.getInt(key, 0)
    }

}